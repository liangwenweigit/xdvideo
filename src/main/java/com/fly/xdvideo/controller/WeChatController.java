package com.fly.xdvideo.controller;

import com.fly.xdvideo.config.WeChatConfig;
import com.fly.xdvideo.domain.JsonData;
import com.fly.xdvideo.domain.User;
import com.fly.xdvideo.domain.VideoOrder;
import com.fly.xdvideo.service.UserService;
import com.fly.xdvideo.service.VideoOrderService;
import com.fly.xdvideo.utils.JwtUtils;
import com.fly.xdvideo.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author liang
 * @date 2019/1/31 - 17:26
 */
@Controller
@RequestMapping("/api/wechat")
public class WeChatController {

    /**
     * 微信配置类注入
     */
    @Autowired
    private WeChatConfig weChatConfig;
    /**
     * 用户业务层对象注入
     */
    @Autowired
    private UserService userService;

    /**
     * 订单业务层对象注入
     */
    @Autowired
    private VideoOrderService videoOrderService;

    /**
     * 拼装微信扫一扫登录url
     * @return
     * access_page参数：是当前所在页面，传过去，就是登陆后返回当前页面,要+http://
     * http://localhost:8088/api/wechat/login_url?access_page=http://www.flyx5.com
     * 返回的json串 就获取url(data)
     */
    @GetMapping("/login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam("access_page")String access_page) throws UnsupportedEncodingException {
        //获取回调地址
        String redirectUrl = weChatConfig.getOpenRedirectUrl();
        //回调url需要处理(官网信息：请使用urlEncode对链接进行处理) ,异常抛出去，我们还要做全局异常处理
        String callbackUrl = URLEncoder.encode(redirectUrl,"GBK");
        //替换%s参数
        String qrcodeUrl = String.format(weChatConfig.getOpenQrcodeUrl().trim(),
                weChatConfig.getOpenAppid().trim(),callbackUrl.trim(),access_page.trim());

        return JsonData.buildSuccess(qrcodeUrl);
    }

    /**
     * 微信登录回调地址
     * @param code 必须的会获取得到
     * @param state 必须的会获取得到
     * @param response 用于转发
     * @return
     */
    @GetMapping("/user/callback")
    public void back(@RequestParam("code") String code, String state, HttpServletResponse response) throws Exception {
//System.out.println(code);//0213N2QL1pknb71vFIPL1sPGPL13N2Qw
//System.out.println(state);//http://www.flyx5.com
        User user = userService.saveWeChatUser(code);
        //判断
        if(user != null){
            //生成jwt
            String token = JwtUtils.createJsonWebToken(user);
            // state 当前用户的页面地址，需要拼接 http://  这样才不会站内跳转,但是传来的数据有http就不用加
            //这里url转码，name第一次是Base64解码，然后有中文，再转成url编码
            String url = state+"?token="+token+"&head_img="+user.getHeadImg()+"&name="+
                    URLEncoder.encode( new String(Base64.getDecoder().decode(user.getName().getBytes()), "UTF-8"),"UTF-8");
         System.out.println(url);

            response.sendRedirect(url);
            return;//一定要retun
        }
        //为空就转到主页
        response.sendRedirect(code);
    }


    /**
     * 微信支付后的回调方法
     */
    @RequestMapping("/wxpay/back")
    public void wxpayCallback(HttpServletRequest request,HttpServletResponse response) throws Exception {

        InputStream inputStream = request.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = br.readLine())!=null){
            sb.append(line);
        }
        //关闭流
        br.close();
        inputStream.close();
        //把微信回调传过来的xml数据 转成map数据
        Map<String, String> map = WXPayUtil.xmlToMap(sb.toString());
        //测试输出（成功）
        System.out.println(map.toString());
        /**
         * {nonce_str=P7n3TgLjtiBXTMiF, code_url=weixin://wxpay/bizpayurl?pr=9tSyWGx, appid=wx5beac15ca207c40c,
         * sign=ECC209CE3E9842084A3BF638254F9F67, trade_type=NATIVE,
         * return_msg=OK, result_code=SUCCESS, mch_id=1503809911, return_code=SUCCESS,
         * prepay_id=wx042025375238476a966d026a3235846780}
         */
        //把获取的map转换成有序的map
        SortedMap<String, String> sortedMap = WXPayUtil.getSortedMap(map);
        //判断签名是否正确,true为正确
        if (WXPayUtil.checkSign(sortedMap,weChatConfig.getKey())){
            //也成功
            System.out.println("输出表示验证sign成功,和回调的一样");
            //判断微信状态码,表示商户接收通知成功并校验成功
            if ("SUCCESS".equals(sortedMap.get("result_code"))){
                //更新订单状态,根据流水号查询订单，之后修改订单状态
                String outTradeNo = sortedMap.get("out_trade_no");
                VideoOrder dbVideoOrder = videoOrderService.findOrderByOutTradeNo(outTradeNo);
                //判断订单是0才支付，才加积分，判断逻辑看业务场景
                if(dbVideoOrder != null && dbVideoOrder.getState()==0){
                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setState(1);//设置为支付状态
                    videoOrder.setNotifyTime(new Date());//设置通知时间
                    videoOrder.setOutTradeNo(outTradeNo);
                    //更新状态 和 通知时间
                    int row = videoOrderService.updateOrderByOutTradeNo(videoOrder);
                    //判断影响行数row==1,响应微信成功或者失败。回应微信，SUCCESS 或者 FAIL
                    if (row == 1){
                        response.setContentType("text/xml");
                        response.getWriter().println("SUCCESS");
                        response.getWriter().flush();
                        response.getWriter().close();
                        return;
                    }
                }
            }
        }
        //这里是都处理失败
        response.setContentType("text/xml");
        response.getWriter().println("FAIL");
        response.getWriter().flush();
        response.getWriter().close();
    }

}
