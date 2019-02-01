package com.fly.xdvideo.controller;

import com.fly.xdvideo.config.WeChatConfig;
import com.fly.xdvideo.domain.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liang
 * @date 2019/1/31 - 16:52
 */
@RestController
@RequestMapping("/test")
public class TestContoller {

    @Autowired
    private WeChatConfig weChatConfig;

    @RequestMapping("/test1")
    public JsonData test1(){
        return JsonData.buildSuccess(weChatConfig.getAppId(),0);
    }
}
