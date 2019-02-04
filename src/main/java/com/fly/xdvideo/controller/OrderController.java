package com.fly.xdvideo.controller;

import com.fly.xdvideo.domain.JsonData;
import com.fly.xdvideo.dto.VideoOrderDto;
import com.fly.xdvideo.service.VideoOrderService;
import com.fly.xdvideo.utils.IpUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
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
     * 必须是get方式 访问：http://localhost:8088/user/api/order/save_order?video_id=1
     * 用户下单，后面新写的
     *
     * @return
     */
    @GetMapping("/save_order")
    public void saveOrder(@RequestParam("video_id") Integer video_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取用下单户IP，使用
        //String ip = IpUtils.getIpAddr(request);
        //测试的时候不能用本地IP，直接写死一个在这里先
        String ip = "192.154.2.32";

        //从request域中获取是谁下的单，一般这个在用户登录的时候就会设置进去
        //我们这里直接写一个固定数据，这个id必须是数据库有的
        Integer user_id = 26;
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        //下面这俩在下面生成订单的时候再处理
        //videoOrderDto.setDel(0);//0表示未删除
        //videoOrderDto.setState(0);//0表示未支付
        videoOrderDto.setUserId(user_id);//用户id
        videoOrderDto.setVideoId(video_id);//订单项视频id
        videoOrderDto.setIp(ip);//下单客户IP地址
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

}
