package com.example.dto;

/**
 * @Author: fei
 * @Description:
 * @Date: Created in 14:17 2017/6/21
 * @Modified by:
 */
public class ParamDto {
    private Integer[] id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer[] getId() {
        return id;
    }

    public void setId(Integer[] id) {
        this.id = id;
    }

}
