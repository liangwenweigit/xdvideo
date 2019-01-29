package com.fly.xdvideo.controller;

import com.fly.xdvideo.config.WeChatConfig;
import com.fly.xdvideo.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author liang
 * @date 2019/1/29 - 20:12
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private WeChatConfig weChatConfig;
    //然后用get获取属性值使用

    @RequestMapping("/testConfig")
    public String testConfig(){
        return weChatConfig.getAppId()+"==="+weChatConfig.getAppsecret();
    }
    @Autowired
    private DataSource dataSource;

    @RequestMapping("/testData")
    public void testData() throws SQLException {
        //class com.alibaba.druid.pool.DruidDataSource
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        //com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@304def36
        System.out.println(connection);
        connection.close();
    }

    @Autowired
    private VideoMapper videoMapper;

    @RequestMapping("/selectAll")
    public Object selectAll(){

        return videoMapper.selectAll();
    }
}

