package com.fly.xdvideo.controller.admin;

import com.fly.xdvideo.domain.Video;
import com.fly.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员才能操作的 video控制层
 * @author liang
 * @date 2019/1/30 - 17:26
 */
@RestController
@RequestMapping("/admin/video")
public class VideoAdminController {

    /**
     * 注入VideoService对象
     */
    @Autowired
    private VideoService videoService;

    /**
     * 根据id更新视频
     * @param video
     *  @RequestBody Video video 页面需要传递json格式的video对象进来
     *  例如：（不用写全部参数）
     *{
    "id":10,
    "title":"weix微信微信支付微信支付的测试",
    "price":99
    }
     */
    //http://localhost:8088/admin/video/update
    @PutMapping("/update")
    public void updateVideoById(@RequestBody Video video) throws Exception {
        videoService.updateVideo(video);
    }

    /**
     * 新增视频
     * @param title
     * @return
     * http://localhost:8088/admin/video/save
     */
    //这个方法注意，在mapper层返回的是 int 或者 void,但是在service层调用处科员直接返回video对象 带自增主键的
    @PostMapping("/save")//新增用post  带一个参数
    public Object saveVideo(@RequestParam("title")String title) throws Exception {
        Video video = new Video();
        video.setTitle(title);
        return videoService.saveVideo(video);
    }

    /**
     * 根据id删除视频
     * @param id
     * http://localhost:8088/admin/video/delete
     */
    @DeleteMapping("/delete")                             //这个表示必须传这个参数,这个默认就是true
    public void deleteVideoById(@RequestParam(value = "id",required = true)Integer id) throws Exception {
        videoService.deleteVideo(id);
    }

}
