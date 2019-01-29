package com.fly.xdvideo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liang
 * @date 2018/12/11 - 18:16
 */
@Configuration
public class DruidConfig {
    //我们自己创建一个数据源
    @ConfigurationProperties(prefix = "spring.datasource")//注入配置属性
    @Bean(destroyMethod = "close", initMethod = "init")//这句不加监控页面会出现（*）druid property for user to setup的错误
    public DataSource dataSource(){//注意方法名
        return new DruidDataSource();
    }

    //配置druid的监控
    //1、配置一个管理后台的servlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //设置初始化参数
        Map<String,String> initParams = new HashMap<>();
        //key都是固定的
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","admin");
        initParams.put("allow","");//默认是允许所有访问
        //initParams.put("deny","192.168.01.11");//拒绝谁访问

        servletRegistrationBean.setInitParameters(initParams);
        return servletRegistrationBean;
    }

    //2、配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        filterRegistrationBean.setFilter(new WebStatFilter());

        //设置初始化参数
        Map<String,String> initParams = new HashMap<>();
        //那些不拦截 静态资源 .js .css .png .jpg等等
        //key都是固定的
        initParams.put("exclusions","*.js,*.css,*.png,*.jpg,/druid/*,*.ico,*.gif,/static/*");
        filterRegistrationBean.setInitParameters(initParams);

        //设置拦截的请求
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));//表示拦截所有

        return filterRegistrationBean;
    }
}
