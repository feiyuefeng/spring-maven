package com.example.solr.vo;

/**
 * Created by Administrator on 2017/8/27.
 */
public class SolrResourceSearchVo {
    /**
     * 0：不筛选 1：100万一下 2:100-150万 3:150万以上
     */
    private Integer price;
    /**
     * 0：不筛选 1:50平米以下 2:50-100平米 3：100平米以上
     */
    private Integer area;
    /**
     * 0：不筛选 1：一室 2:二室 3：三室 4：四室 5：四室以上
     */
    private Integer room;

    /**
     * 1：面积正序 2：面积倒序 3：价格正序 4：价格倒序 5：发布时间正序 6：发布时间倒序

     */
    private Integer sort;
    private Integer page;
    private Integer pageSize;

    public SolrResourceSearchVo(){}
    public SolrResourceSearchVo(Integer price, Integer area, Integer room, Integer page, Integer pageSize, Integer sort){
        this.page = page;
        this.price = price;
        this.area = area;
        this.room = room;
        this.sort = sort;
        this.pageSize = pageSize;

    }
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
