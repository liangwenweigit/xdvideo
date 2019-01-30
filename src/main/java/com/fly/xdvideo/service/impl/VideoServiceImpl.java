package com.fly.xdvideo.service.impl;

import com.fly.xdvideo.domain.Video;
import com.fly.xdvideo.mapper.VideoMapper;
import com.fly.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liang
 * @date 2019/1/28 - 23:01
 */
@Service
public class VideoServiceImpl implements VideoService {
    /**
     * 注入mapper接口对象
     */
    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> selectAllVideo() {
        return videoMapper.selectAllVideo();
    }

    @Override
    public Video findVideoById(Integer id) {
        return videoMapper.findVideoById(id);
    }

    @Override
    public void updateVideo(Video video) {
        videoMapper.updateVideo(video);
    }

    @Override
    public void deleteVideo(Integer id) {
        videoMapper.deleteVideo(id);
    }

    @Override
    public Video saveVideo(Video video) {
        videoMapper.saveVideo(video);
        return video;
    }
}
