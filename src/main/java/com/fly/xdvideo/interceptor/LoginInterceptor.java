package com.fly.xdvideo.interceptor;

import com.fly.xdvideo.domain.JsonData;
import com.fly.xdvideo.utils.JwtUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录拦截器
 * @author liang
 * @date 2019/2/1 - 22:33
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 这个工具类需要这个依赖
     gson工具，封装http的时候使用
     <dependency>
     <groupId>com.google.code.gson</groupId>
     <artifactId>gson</artifactId>
     <version>2.8.0</version>
     </dependency>
     */
    private static final Gson gson = new Gson();

    /**
     * 进入controller之前拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //首先先拿到tocken,tocken有可能放在请求头，也有可能是一个token参数
        String tocken = request.getHeader("tocken");//获取请求头的看看有没有tocken
        if (tocken==null){
            tocken = request.getParameter("tocken");//在获取参数里面的tocken看看有没有
        }

        if (tocken !=null){
            Claims claims =  JwtUtils.checkTocken(tocken);
            if(claims !=null){
                Integer userId = (Integer)claims.get("id");
                String name = (String) claims.get("name");

                request.setAttribute("user_id",userId);
                request.setAttribute("name",name);
                //放行
                return true;
            }
        }

        //来到这里表示拦截
       //这里可以转发到登录页，也可以给前端返回json数据，这里返回json数据
        //在下面一个方法 写json数据出去给前端
        //并且使用我们之前封装的JsonData工具类 带转态码的
        sendJsonMessage(response, JsonData.buildError("请登录"));

        //拦截
        return false;
    }

    /**
     * 响应数据给前端
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws IOException {

        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(gson.toJson(obj));
        writer.close();
        response.flushBuffer();

    }
}
