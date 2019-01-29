package com.fly.xdvideo.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * 评论 实体类
 */
public class Comment implements Serializable {

  private Integer id;
  private String content;
  private Integer userId;
  private String headImg;
  private String name;
  private double point;
  private Integer up;
  private java.util.Date createTime;
  private Integer orderId;
  private Integer videoId;

  public Comment() {
  }

  public Comment(Integer id, String content, Integer userId, String headImg, String name, double point, Integer up, Date createTime, Integer orderId, Integer videoId) {

    this.id = id;
    this.content = content;
    this.userId = userId;
    this.headImg = headImg;
    this.name = name;
    this.point = point;
    this.up = up;
    this.createTime = createTime;
    this.orderId = orderId;
    this.videoId = videoId;
  }

  @Override
  public String toString() {
    return "Comment{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", userId=" + userId +
            ", headImg='" + headImg + '\'' +
            ", name='" + name + '\'' +
            ", point=" + point +
            ", up=" + up +
            ", createTime=" + createTime +
            ", orderId=" + orderId +
            ", videoId=" + videoId +
            '}';
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public String getHeadImg() {
    return headImg;
  }

  public void setHeadImg(String headImg) {
    this.headImg = headImg;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public double getPoint() {
    return point;
  }

  public void setPoint(double point) {
    this.point = point;
  }


  public Integer getUp() {
    return up;
  }

  public void setUp(Integer up) {
    this.up = up;
  }


  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }


  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }


  public Integer getVideoId() {
    return videoId;
  }

  public void setVideoId(Integer videoId) {
    this.videoId = videoId;
  }

}