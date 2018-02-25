package com.example.Result;

/**
 * Created by fyf on 2017/8/28.
 */
public class ErrorParamResult {
    private String errorColumn;
    private String msg;

    public ErrorParamResult(String errorColumn, String msg) {
        this.errorColumn = errorColumn;
        this.msg = msg;
    }
    public String getErrorColumn() {
        return errorColumn;
    }

    public void setErrorColumn(String errorColumn) {
        this.errorColumn = errorColumn;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
