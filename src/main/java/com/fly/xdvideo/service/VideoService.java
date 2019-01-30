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

    List<Video> selectAllVideo();

    Video findVideoById(Integer id);

    void updateVideo(Video video);

    void deleteVideo(Integer id);

    Video saveVideo(Video video);
}
