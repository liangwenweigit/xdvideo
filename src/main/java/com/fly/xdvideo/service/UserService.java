package com.fly.xdvideo.service;

import com.fly.xdvideo.domain.User;

/**
 * 用户业务层接口类
 * @author liang
 * @date 2019/2/1 - 13:29
 */
public interface UserService {

    User saveWeChatUser(String code);
}
