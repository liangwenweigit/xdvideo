package com.fly.xdvideo.mapper;

import com.fly.xdvideo.domain.VideoOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author liang
 * @date 2019/2/2 - 20:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoOrderMapperTest {

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Test
    public void insert() {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setHeadImg("测试");
        videoOrder.setUserId(10);
        videoOrder.setDel(0);
        videoOrder.setState(0);
        videoOrderMapper.insert(videoOrder);
    }

    @Test
    public void findOrderById() {
        VideoOrder videoOrder = videoOrderMapper.findOrderById(1);
        System.out.println(videoOrder);
    }

    @Test
    public void findOrderByOutTradeNo() {
        VideoOrder videoOrder = videoOrderMapper.findOrderByOutTradeNo("3432");
        System.out.println(videoOrder);
    }

    @Test
    public void delOrder() {
        videoOrderMapper.delOrder(5,10);
    }

    @Test
    public void findMyOrderList() {
        List<VideoOrder> orderList = videoOrderMapper.findMyOrderList(1);
        for(VideoOrder videoOrder:orderList){System.out.println(videoOrder);}

    }

    @Test
    public void updateOrderByOutTradeNo() {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setState(1);
        videoOrder.setNotifyTime(new Date());
        videoOrder.setOutTradeNo("3432");
        videoOrderMapper.updateOrderByOutTradeNo(videoOrder);
    }
}