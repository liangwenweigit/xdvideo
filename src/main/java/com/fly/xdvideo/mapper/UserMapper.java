package com.fly.xdvideo.mapper;

import com.fly.xdvideo.domain.User;
import com.fly.xdvideo.provider.UserMapperDynaSQLCreater;
import org.apache.ibatis.annotations.*;

/**
 * 用户mapper接口
 * @author liang
 * @date 2019/2/1 - 16:21
 */
@Mapper
public interface UserMapper {

    /**
     * id
     * 根据id查询用户
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id) throws Exception;
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User findByopenid(@Param("openid") String openid) throws Exception;

    /**
     * 保存用户
     * @param user
     * @return
     */
    @Insert("insert into user(openid,name,head_img,phone,sign,sex,city,create_time) " +
            "values(#{openid},#{name},#{headImg},#{phone},#{sign},#{sex},#{city},#{createTime})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void saveUser(User user) throws Exception;

    /**
     * 更新用户到数据库
     * @param user
     */
    @UpdateProvider(type = UserMapperDynaSQLCreater.class,method = "updateUser")
    void updateUser(User user) throws Exception;
}
