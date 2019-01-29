package com.fly.xdvideo.mapper;

import com.fly.xdvideo.domain.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 视频mapper接口
 * @author liang
 * @date 2019/1/28 - 23:03
 */
@Mapper
public interface VideoMapper {

    @Select("select * from video")
    List<Video> selectAll();
}
