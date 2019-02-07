package com.fly.xdvideo.exception;

/**
 * 全局异常类
 * @author liang
 * @date 2019/2/5 - 15:30
 */
public class XdException extends RuntimeException{

    /**
     *转态码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String msg;

    public XdException() {
        super();
    }

    public XdException(Integer code) {
        this.code = code;
    }

    public XdException(String msg) {
        this.msg = msg;
    }

    public XdException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
