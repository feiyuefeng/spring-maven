package com.example.solr.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/27.
 */
public class SolrResourceVo implements Serializable{
    private Long id;
    private Double price;
    private Double area;
    private Integer room;
    private Date publish_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }
}
