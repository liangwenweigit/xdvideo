package com.fly.xdvideo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//mybatis指定批量扫描的mapper包
@MapperScan("com.fly.xdvideo.mapper")
public class XdvideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(XdvideoApplication.class, args);
	}

}

