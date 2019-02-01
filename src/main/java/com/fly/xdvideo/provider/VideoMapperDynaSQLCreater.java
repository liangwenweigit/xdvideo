package com.fly.xdvideo.provider;

import com.fly.xdvideo.domain.Video;
import org.apache.ibatis.jdbc.SQL;

/**
 * videoMapper接口 动态sql提供类
 * @author liang
 * @date 2019/1/30 - 18:31
 */
public class VideoMapperDynaSQLCreater {
    /**
     * 更新videoSQL
     * @param video
     * @return
     */
    public String updateVideo(final Video video){
        return new SQL(){{
            UPDATE("video");
            if (video.getTitle()!=null){
                SET("title = #{title}");
            }
            if (video.getSummary()!=null){
                SET("summary = #{summary}");
            }
            if (video.getCoverImg()!=null){
                SET("cover_img = #{coverImg}");
            }
            if (video.getViewNum()!=null){
                SET("view_num = #{viewNum}");
            }
            if (video.getPrice()!=null){
                SET("price = #{price}");
            }
            if (video.getCreateTime()!=null){
                SET("create_time = #{CreateTime}");
            }
            if (video.getOnline()!=null){
                SET("online = #{online}");
            }
            //这里记得把实体类的基本类型改成 对象包装类型 才能判断是否是null
            if (video.getPoint()!=null){
                SET("point = #{point}");
            }
            WHERE("id = #{id}");
        }}.toString();
    }
}
