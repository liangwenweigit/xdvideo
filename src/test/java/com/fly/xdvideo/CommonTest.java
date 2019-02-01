package com.fly.xdvideo;

import com.fly.xdvideo.domain.User;
import com.fly.xdvideo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author liang
 * @date 2019/1/31 - 12:46
 */
public class CommonTest {

    @Test
    public void testJwt1(){
        User user = new User();
        user.setId(999);
        user.setHeadImg("image1/");
        user.setName("name/name/");

        String token = JwtUtils.createJsonWebToken(user);
        System.out.println(token);
        //输出下面一串
        //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZHZpZGVvIiwiaWQiOjk5OSwibmFtZSI6Im5hbWUvbmFtZS8iLCJpbWFnZSI6ImltYWdlMS8iLCJpYXQiOjE1NDg5MTEyMTAsImV4cCI6MTU0OTUxNjAxMH0.km2dTBgszvnYQnCwnUK9UjNJvu5QlNwBrqULBdPe2W0
    }

    @Test
    public void testJwt2(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZHZpZGVvIiwiaWQiOjk5OSwibmFtZSI6Im5hbWUvbmFtZS8iLCJpbWFnZSI6ImltYWdlMS8iLCJpYXQiOjE1NDg5MTEyMTAsImV4cCI6MTU0OTUxNjAxMH0.km2dTBgszvnYQnCwnUK9UjNJvu5QlNwBrqULBdPe2W0";
        Claims claims = JwtUtils.checkTocken(token);
        //写业务的时候要判断是否为空
        System.out.println(claims);
        if (claims!=null){
            //注意这里获取的必须是工具类里面定义的
            //.claim("id", user.getId())
            String name = (String) claims.get("name");
            String image = (String) claims.get("image");
            Integer id = (Integer) claims.get("id");
            System.out.println(name+","+image+","+id);
        }

    }
    @Test//编码Base64
    public void test1() throws UnsupportedEncodingException {
      String name = "a飞飞";
      String temp = new String(Base64.getEncoder().encode(name.getBytes()),"UTF-8");
        System.out.println(temp);
    }

    @Test//解码Base64
    public void test2() throws UnsupportedEncodingException {
        String temp = "8J+RifCfkarwn5GI";//👉👪👈
        String name = new String(Base64.getDecoder().decode(temp.getBytes()),"UTF-8");
        System.out.println(name);
    }
}
