package com.fly.xdvideo.service.impl;

import com.fly.xdvideo.config.WeChatConfig;
import com.fly.xdvideo.domain.User;
import com.fly.xdvideo.mapper.UserMapper;
import com.fly.xdvideo.service.UserService;
import com.fly.xdvideo.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
/**
 * 用户业务层实现类
 * @author liang
 * @date 2019/2/1 - 13:34
 */
@Service
public class UserServiceImpl implements UserService{

    /**
     * 把微信配置类注入进来 获取里面的配置
     */
    @Autowired
    private WeChatConfig weChatConfig;
    /**
     * 用户mapper接口对象注入
     */
    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveWeChatUser(String code) {//静态方法获取属性
        String accessTokenUrl = String.format(WeChatConfig.getOpenAccessTokenUrl(),weChatConfig.getOpenAppid(),weChatConfig.getOpenAppsecret(),code);

        //获取access_token
        Map<String ,Object> baseMap =  HttpUtils.doGet(accessTokenUrl);

        if(baseMap == null || baseMap.isEmpty()){ return  null; }
        String accessToken = (String)baseMap.get("access_token");
        String openId  = (String) baseMap.get("openid");

        User dbUser = userMapper.findByopenid(openId);

        if(dbUser!=null) { //更新用户，直接返回
System.out.println("=============用户不是第一次访问===========");
            return dbUser;
        }
System.out.println("===========用户第一次访问===========保存数据库");
        //获取用户基本信息
        String userInfoUrl = String.format(WeChatConfig.getOpenUserInfoUrl(),accessToken,openId);
        //获取access_token
        Map<String ,Object> baseUserMap =  HttpUtils.doGet(userInfoUrl);

        if(baseUserMap == null || baseUserMap.isEmpty()){ return  null; }
        /**
         openid	普通用户的标识，对当前开发者帐号唯一
         nickname	普通用户昵称
         sex	普通用户性别，1为男性，2为女性
         province	普通用户个人资料填写的省份
         city	普通用户个人资料填写的城市
         country	国家，如中国为CN
         headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
         privilege	用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
         unionid	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
         */
        //获取用户信息
        String nickname = (String)baseUserMap.get("nickname");//用户名称，需要转码

        //防止浏览器点击一次，会触发2次访问解决办法，第二次name肯定是null,直接返回null
        if (nickname==null || "".equals(nickname)){
            return null;
        }


        //用户性别
        Double sexTemp  = (Double) baseUserMap.get("sex");//性别返回来是double类型,必要时才用
        Integer sex = null;
        if (sexTemp!= null && sexTemp!= 0.0){
            sex = sexTemp.intValue();
        }

        String country = (String)baseUserMap.get("country");//国家，如中国为CN
        String province = (String)baseUserMap.get("province");//省份
        String city = (String)baseUserMap.get("city");//城市
        String headimgurl = (String)baseUserMap.get("headimgurl");//用户头像

        //地址拼接：例子：中国||广东||广州，需要转码,下面会转
        StringBuilder sb = new StringBuilder("");
        if (country!=null && !"".equals(country)){
            sb.append(country);
        }
        if (province!=null && !"".equals(province)){
            sb.append("||").append(province);
        }
        if (city!=null && !"".equals(city)){
            sb.append("||").append(city);
        }
        String finalAddress = sb.toString();

        try {//解决乱码
            if (nickname!=null){
                nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            }
            finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        User user = new User();
        try {
            //防止有特殊符号name，保存之前进行base64转码，取出来进行base64解码
            //如果是通用逻辑，那就全部用户都可以存进去进行base64转码，取出来进行base64解码
                user.setName(new String(Base64.getEncoder().encode(nickname.getBytes()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenid(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());
        userMapper.saveUser(user);

        //测试name解码 ：上线去除
        //从数据库把name取出来,进行base64解码,解码前先要判断是否为空，要不然爆空指针异常
        try {
            System.out.println(new String(Base64.getDecoder().decode(user.getName().getBytes()), "UTF-8")+",解码64");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return user;
    }
}
