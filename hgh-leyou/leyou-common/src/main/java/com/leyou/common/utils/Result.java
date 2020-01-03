package com.leyou.common.utils;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public Result() {
        this.code = 0;
        this.msg = "success";
        this.data = null;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

}
