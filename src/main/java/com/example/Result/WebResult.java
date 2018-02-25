package com.example.Result;

import java.io.Serializable;

/**
 * Created by fyf on 2017/8/28.
 */
public class WebResult implements Serializable {
    private Integer status;
    private String msg;
    private Object result;

    public WebResult(Integer status, String msg, Object result){
        this.status = status;
        this.msg = msg;
        this.result = result;
    }

    public WebResult(Integer status, Object result){
        this.status = status;
        this.result = result;
    }

    public WebResult(Integer status) {
        this.status = status;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
