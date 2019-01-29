package com.fly.xdvideo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 微信配置类
 * @author liang
 * @date 2019/1/29 - 19:52
 */
//@Configuration 指明这是一个配置类
@Configuration
//@PropertySource 指明从哪里读取配置
@PropertySource(value = "classpath:application.properties")
public class WeChatConfig {

    /**
     * 不要导错包
     * import org.springframework.beans.factory.annotation.Value;
     */
    /**
     * 公众号appId
     */
    @Value("${wxpay.appid}")
    private String appId;
    /**
     * 公众号秘钥
     */
    @Value("${wxpay.appsecret}")
    private String appsecret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }
}
