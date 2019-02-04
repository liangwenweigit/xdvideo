package com.fly.xdvideo.controller;

import com.fly.xdvideo.domain.Video;
import com.fly.xdvideo.service.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 管理员通用  video控制层
 * @author liang
 * 2019年1月28日下午9:38:42
 */
@RestController
@RequestMapping("/video")
public class VideoController {
	/**
	 * 注入VideoService对象
	 */
	@Autowired
	private VideoService videoService;

	/**
	 * restful规范
	 * @PathVariable用于将请求URL中的模板变量映射到功能处理方法的参数上。
	 * //配置url和方法的一个关系@RequestMapping("item/{itemId}")
	 */
	/**
	 * 分页接口
	 * @param page 当前页数，默认是第1页
	 * @param size 每页显示几条，默认是10条
	 * @return
	 * http://localhost:8088/video/page
     * http://localhost:8088/video/page?page=2
	 */
	@GetMapping("/page")
	public Object pageVideo(@RequestParam(value = "page",defaultValue = "1")Integer page,
							@RequestParam(value = "size",defaultValue = "10")Integer size) throws Exception {
	    //使用依赖内置对象
        PageHelper.startPage(page,size);
        //这里直接调用查询所有的sql即可，就是下面这个
        //    @Select("select * from video")
        //    List<Video> selectAllVideo();
        List<Video> videos = videoService.selectAllVideo();
        //把结果集封装进去
        PageInfo<Video> pageInfo = new PageInfo<>(videos);
        //这里开发的时候把需要的属性取出来 返回也可以
        //pageInfo.getXXX   getList就是上面的集合
        //比如我们这里抽取需要的数据返回给前端
        Map<String,Object> maps = new HashMap<>(0);
        maps.put("videos",pageInfo.getList());//取返回的数据集合
        maps.put("total_size",pageInfo.getTotal());//获取总条数
        maps.put("total_page",pageInfo.getPages());//获取总页数
        maps.put("page_size",pageInfo.getSize());//每页大小
        maps.put("firstPage",pageInfo.getFirstPage());//第一页
        maps.put("lastPage",pageInfo.getLastPage());//最后一页
        maps.put("prePage",pageInfo.getPrePage());//前一页
        maps.put("nextPage",pageInfo.getNextPage());//下一页
        return maps;
	}

	/**
	 * 查询所有视频
	 * @return
	 * http://localhost:8088/video/find_all
	 */
	@GetMapping("/find_all")
	public Object findAll() throws Exception {
		return videoService.selectAllVideo();
	}

	/**
	 * 根据id查询视频
	 * @param id
	 * @return
	 * http://localhost:8088/video/find_video?id=1
	 */
	@GetMapping("/find_video")                             //这个表示必须传这个参数,这个默认就是true
	public Object findVideoById(@RequestParam(value = "id",required = true) Integer id) throws Exception {
		return videoService.findVideoById(id);
	}


}
