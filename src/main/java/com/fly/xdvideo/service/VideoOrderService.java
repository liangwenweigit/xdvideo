package com.fly.xdvideo.service;

import com.fly.xdvideo.domain.VideoOrder;
import com.fly.xdvideo.dto.VideoOrderDto;

/**
 * 订单业务层接口
 * @author liang
 * @date 2019/2/3 - 14:37
 */
public interface VideoOrderService {

    /**
     * 保存订单
     * 下面的参数也可以封装成一个包装类对象，这里就封装了下面的VideoOrderDto,看下面的方法
     * @param user_id
     * @param video_id
     * @param ip
     * @return
     */
    //VideoOrder saveOrder(Integer user_id,Integer video_id,String ip);

    /**
     * 保存订单
     * 接上面方法的，这个是封装了参加成包装类的
     * @param videoOrderDto 参数包装类
     * @return
     */
    VideoOrder saveOrder(VideoOrderDto videoOrderDto) throws Exception;

}
