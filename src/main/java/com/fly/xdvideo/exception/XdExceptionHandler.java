package com.fly.xdvideo.exception;

import com.fly.xdvideo.domain.JsonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理类
 * @author liang
 * @date 2019/2/5 - 15:51
 */
@ControllerAdvice//标明这是一个全局异常处理类
public class XdExceptionHandler {
    //这个是命中info文件的
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ExceptionHandler(value = Exception.class)//这里表示拦截全部异常
    @ResponseBody
    public JsonData handler(Exception e){
        if (e instanceof XdException){
            XdException xdException = (XdException) e;
            return JsonData.buildError(xdException.getMsg(),xdException.getCode());
        }
        //其他未知异常 打印 后台
        e.printStackTrace();

        //打印到自己的错误日志文件//这里必须配置了 logback-spring.xml 日志文件才有用的
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw =  new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        //异常打印到自己的日志  提示错误一样可以用的
        logger.error(sw.toString());

        //并且发送异常信息给前端
        return JsonData.buildError("服务器正在维护，请稍后访问!");
    }

}
