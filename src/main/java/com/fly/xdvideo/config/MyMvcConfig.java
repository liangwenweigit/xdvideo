package com.fly.xdvideo.config;

import com.fly.xdvideo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 * @author liang
 * @date 2019/2/1 - 23:49
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    /**
     * 添加登录拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
                                                       //添加拦截的请求url（原来是/user/api/order/**）
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/1/user/api/order/**")
                //设置不拦截的请求url
                .excludePathPatterns();
        //注意：静态页面不需要处理，springboot已经处理了
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}

