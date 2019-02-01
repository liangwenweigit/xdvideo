package com.fly.xdvideo.utils;

import com.fly.xdvideo.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt工具类 产生tocken 校验解密tocken
 *
 * @author liang
 * @date 2019/1/30 - 22:46
 */
public class JwtUtils {
    //一般写公司的 ，或者应用名
    public static final String SUBJECT = "xdvideo";
    //定义过期时间,毫秒。1周
    public static final long EXPIRTION = 1000 * 60 * 60 * 24 * 7;
    //秘钥(自定义 但是一般开发和生产 不一样 不能泄露)
    public static final String APPSECRET = "flyfly";

    private JwtUtils() {
    }

    /**
     * 产生token
     *
     * @param user 用户
     * @return
     */
    public static String createJsonWebToken(User user) {
        //校验
        if (user == null || user.getId() == null || user.getName() == null || user.getHeadImg() == null) {
            return null;
        }
        String tocken = Jwts.builder().setSubject(SUBJECT) //应用所有者
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("image", user.getHeadImg())//需要的属性还可以继续这样加
                .setIssuedAt(new Date())//发行时间，就这样固定就可以了
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRTION))//设置tocken过期时间
                .signWith(SignatureAlgorithm.HS256, APPSECRET)//设置签名 固定就好 ，就是后面秘钥参数可以改
                .compact();
        return tocken;
    }

    /**
     * 校验解密tocken
     *
     * @param tocken
     * @return
     */
    public static Claims checkTocken(String tocken) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(tocken).getBody();
            return claims;
            //当过期 或者黑客仿造 tocken 就抛异常就返回null，也不用 输出错误
        } catch (Exception e) {
            return null;
        }
    }
}
