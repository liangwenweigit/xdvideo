package com.fly.xdvideo.controller;

import com.fly.xdvideo.config.WeChatConfig;
import com.fly.xdvideo.domain.JsonData;
import com.fly.xdvideo.domain.User;
import com.fly.xdvideo.service.UserService;
import com.fly.xdvideo.utils.JwtUtils;
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

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
    public void back(@RequestParam("code") String code, String state, HttpServletResponse response) throws IOException {
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
}
