package com.fly.xdvideo.service;

import com.fly.xdvideo.domain.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author liang
 * @date 2019/2/2 - 20:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Test
    public void selectAllVideo() {
        List<Video> videos = videoService.selectAllVideo();
        //断言
        assertNotNull(videos);
        for(Video video:videos){
            System.out.println(video);
        }

    }

    @Test
    public void findVideoById() {
        Video video = videoService.findVideoById(1);
        //断言
        assertNotNull(video);
        System.out.println(video);
    }

    @Test
    public void updateVideo() {
        Video video = new Video();
        video.setSummary("单元测试修改概要");
        video.setId(1);
        videoService.updateVideo(video);
    }

    @Test
    public void deleteVideo() {
        videoService.deleteVideo(12);
    }

    @Test
    public void saveVideo() {
        Video video = new Video();
        video.setTitle("单元测试");
        videoService.saveVideo(video);
    }
}