package com.fly.xdvideo.provider;

import com.fly.xdvideo.domain.User;
import org.apache.ibatis.jdbc.SQL;

import java.util.Set;

/**
 * 用户mapper接口 动态SQL提供类
 * @author liang
 * @date 2019/2/2 - 12:45
 */
public class UserMapperDynaSQLCreater {

    /**
     * 根据id更新用户
     * @param user
     * @return
     */
    public String updateUser(User user){
        return new SQL(){{
            UPDATE("user");
            if (user.getOpenid()!=null){
                SET("openid = #{openid}");
            }
            if (user.getName()!=null){
                SET("name = #{name}");
            }
            if (user.getHeadImg()!=null){
                SET("head_img = #{headImg}");
            }
            if (user.getPhone()!=null){
                SET("phone = #{phone}");
            }
            if (user.getSign()!=null){
                SET("sign = #{sign}");
            }
            if (user.getSex()!=null){
                SET("sex = #{sex}");
            }
            if (user.getCity()!=null){
                SET("city = #{city}");
            }
            if (user.getCreateTime()!=null){
                SET("create_time = #{createTime}");
            }
            WHERE("id = #{id}");
        }}.toString();
    }
}
