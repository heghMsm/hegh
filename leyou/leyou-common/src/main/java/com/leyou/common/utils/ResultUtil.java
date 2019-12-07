package com.leyou.common.utils;


import com.leyou.common.enums.ExceptionEnum;

public class ResultUtil {
    public static Result succ(Object object) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("success");
        result.setData(object);
        return result;
    }

    public static Result succ() {
        return succ(null);
    }

    public static Result fail(ExceptionEnum codeEnum) {
        Result result = new Result();
        result.setCode(codeEnum.getCode());
        result.setMsg(codeEnum.getMsg());
        return result;
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
