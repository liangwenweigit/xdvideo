package com.fly.xdvideo.mapper;

import com.fly.xdvideo.domain.Video;
import com.fly.xdvideo.provider.VideoMapperDynaSQLCreater;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 视频mapper接口
 * @author liang
 * @date 2019/1/28 - 23:03
 */
@Mapper
public interface VideoMapper {

    @Select("select * from video")
    List<Video> selectAllVideo();

    @Select("select * from video where id = #{id}")
    Video findVideoById(@Param("id") Integer id);

    @UpdateProvider(type = VideoMapperDynaSQLCreater.class,method = "updateVideo")
    void updateVideo(Video Video);

    @Delete("DELETE FROM video WHERE id =#{id}")
    void deleteVideo(@Param("id") Integer id);

    @Insert("INSERT INTO `video` ( `title`, `summary`, " +
            "`cover_img`, `view_num`, `price`, `create_time`," +
            " `online`, `point`)" +
            "VALUES" +
            "(#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price},#{createTime}" +
            ",#{online},#{point});")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    //因为封装插入后 指定Options自增id， mybatis 会自动封装 包含ID的对象回来
    void saveVideo(Video video);

}
