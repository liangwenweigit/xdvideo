package com.fly.xdvideo.mapper;

import com.fly.xdvideo.domain.VideoOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单mapper 接口
 *
 * @author liang
 * @date 2019/2/2 - 18:50
 */
@Mapper
public interface VideoOrderMapper {

    /**
     * 新增订单  要返回带主键的订单要用@Options 在内存返回带主键的订单
     *
     * @param videoOrder
     */
    @Insert("insert into video_order (openid,out_trade_no,state,create_time,notify_time," +
            "total_fee,nickname,head_img,video_id,video_title,video_img,user_id,ip,del)" +
            "values (#{openid},#{outTradeNo},#{state},#{createTime},#{notifyTime},#{totalFee}," +
            "#{nickname},#{headImg},#{videoId},#{videoTitle},#{videoImg},#{userId},#{ip},#{del})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(VideoOrder videoOrder);

    /**
     * 根据订单id查询订单
     * 并且是没有删除的默认del=0的订单，就是没删除的   1代表以及删除
     * @param id
     * @return
     */
    @Select("select * from video_order where id = #{id} and del = 0")
    VideoOrder findOrderById(@Param("id") Integer id);

    /**
     *根据交易号获取订单
     * @param
     * @return
     */
    @Select("select * from video_order where out_trade_no = #{out_trade_no} and del = 0")
    VideoOrder findOrderByOutTradeNo(@Param("out_trade_no") String outTradeNo);

    /**
     * 根据订单id+userId删除订单（把del改成1）
     * 因为根据2个id才能删，防止别人非法操作
     * @param id
     * @param userId
     */
    @Update("update video_order set del = 1 where id = #{id} and user_id = #{userId}")
    void delOrder(@Param("id") Integer id,@Param("userId") Integer userId);

    /**
     * 根据用户id查询某个用户的所有订单
     * @param userId
     * @return
     */
    @Select("select * from video_order where user_id = #{userId} and del = 0")
    List<VideoOrder> findMyOrderList(Integer userId);

    /**
     * 根据订单交易号修改订单支付状态
     * 状态：0表示未支付 1表示已支付
     * @param videoOrder
     */
    @Update({"update video_order set state = #{state} , notify_time = #{notifyTime} where out_trade_no = #{outTradeNo} and del = 0 and state = 0" })
    void updateOrderByOutTradeNo(VideoOrder videoOrder);
}
