package com.fly.xdvideo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * video控制层
 * @author liang
 * 2019年1月28日下午9:38:42
 */
@RestController
@RequestMapping("/video")
public class VideoController {
	//http://localhost:8080/video/find
	@RequestMapping("/find")
	public String find(){
		
		return "hello world!!!";
	}

}
