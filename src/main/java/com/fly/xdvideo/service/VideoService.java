package com.fly.xdvideo.service;

import com.fly.xdvideo.domain.Video;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liang
 * @date 2019/1/28 - 23:01
 */
public interface VideoService {

    List<Video> selectAllVideo() throws Exception;

    Video findVideoById(Integer id) throws Exception;

    void updateVideo(Video video) throws Exception;

    void deleteVideo(Integer id) throws Exception;

    Video saveVideo(Video video) throws Exception;
}
