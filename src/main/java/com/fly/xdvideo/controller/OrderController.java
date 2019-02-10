package com.fly.xdvideo.controller;

import com.fly.xdvideo.domain.JsonData;
import com.fly.xdvideo.domain.VideoOrder;
import com.fly.xdvideo.dto.VideoOrderDto;
import com.fly.xdvideo.service.VideoOrderService;
import com.fly.xdvideo.utils.CommonUtils;
import com.fly.xdvideo.utils.IpUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.javafx.collections.MappingChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 下单 控制层
 *
 * @author liang
 * @date 2019/2/2 - 12:20
 */
@RestController
@RequestMapping("/user/api/order")
public class OrderController {

    //这个是命中info文件的
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //这个是命中data数据统计的,参数必须是配置文件里面配置一样的的
    private Logger dataLogger = LoggerFactory.getLogger("dataLogger");

    @Autowired
    private VideoOrderService videoOrderService;

    /**
     * http://localhost:8088/user/api/order/save_order
     * 因为拦截器设置了拦截 所以没有登录被拦截 提示：{"code":-1,"msg":"请登录"}
     * 因为没带tocken，或者tocken是假的
     *
     * 我们通过扫一扫微信登录 获取一个tocken
     * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZHZpZGVvIiwiaWQiOjI2LCJuYW1lIjoiWWVtam51bWpuZz09IiwiaW1hZ2UiOiJodHRwOi8vdGhpcmR3eC5xbG9nby5jbi9tbW9wZW4vdmlfMzIvSE4zeG1pY252M2JjR0huazBuOGZMeWljd2FQNVNhekJHYzczUHo1bXF5UWg0aWI3NVA5SVF1S2NHYkhJdGRCUlFJTW5PUXpENVRXVkxtZFdvT2pwQWdHM1EvMTMyIiwiaWF0IjoxNTQ5MDg1MTAxLCJleHAiOjE1NDk2ODk5MDF9.N4CD92G2FFz2QvIh5zl2PElwEgLwktTuHL8EAWzBcxI
     * 再带tocken访问http://localhost:8088/user/api/order/save_order?tocken=xxx 显示下单成功
     * @return
     */
    //@GetMapping("/save_order")
    //public JsonData saveOrder(){
    //return JsonData.buildSuccess("下单成功");
    //}

    /**
     * 必须是get方式 访问：http://localhost:8088/user/api/order/save_order?video_id=1&uuid=xxx
     * 用户下单，写出付款二维码到客户端
     * 因为是获取图片 所以是get方式
     * @return
     */
    @GetMapping("/save_order")
    public void saveOrder(@RequestParam("video_id") Integer video_id,@RequestParam("orderuuid")String orderuuid,HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("用户下单"+orderuuid+","+video_id);
        //获取用下单户IP，使用
        String ip = IpUtils.getIpAddr(request);
        //测试的时候不能用本地IP，直接写死一个在这里先
        //String ip = "192.168.1.2";

        //从request中获取用户id，因为在拦截器拦截的时候 把id设置进去了
        Integer user_id = (Integer) request.getAttribute("user_id");

        //我们这里直接写一个固定数据，这个id必须是数据库有的
        //Integer user_id = 26;

        //日志统计数据(后面都是自定义的数据) 后面大括号的参数就是 后面的参数(多参数就按顺序写就OK，注意有大括号)
        dataLogger.info("module=OrderController api=save_order user_id={} video_id={}",user_id,video_id);

        VideoOrderDto videoOrderDto = new VideoOrderDto();
        //下面这俩在下面生成订单的时候再处理
        //videoOrderDto.setDel(0);//0表示未删除
        //videoOrderDto.setState(0);//0表示未支付
        videoOrderDto.setUserId(user_id);//用户id
        videoOrderDto.setVideoId(video_id);//订单项视频id
        videoOrderDto.setIp(ip);//下单客户IP地址
        videoOrderDto.setOutTradeNo(orderuuid);//流水号

        //把流水号存进去session 方便后面轮询订单状态
        request.getSession().setAttribute("out_trade_no",videoOrderDto.getOutTradeNo());

        String code_url = videoOrderService.saveOrder(videoOrderDto);

        if (code_url == null) {
            throw new NullPointerException();
        }
        //生成二维码配置
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //设置纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        //编码类型
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, 400, 400, hints);
        OutputStream out = response.getOutputStream();


        //把二维码写出客户端
        MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
        //后面用户支付之后就是回调地址，回调地址的controller路径写在WeChatController里面

    }


    /**
     *轮询订单状态 根据订单流水号
     * @return
     */
    @GetMapping("/select_order")
    public JsonData selectOrderStatu(@RequestParam("out_trade_no") String out_trade_no) throws Exception {
        VideoOrder videoOrder = videoOrderService.findOrderByOutTradeNo(out_trade_no);
        System.out.println(videoOrder);
        if (videoOrder==null){
            return JsonData.buildError("订单不存在");
        }
        if (videoOrder.getState()==1){//支付成功
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("订单未支付");
    }


}
