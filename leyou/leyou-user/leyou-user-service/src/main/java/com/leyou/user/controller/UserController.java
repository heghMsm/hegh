package com.leyou.user.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.common.utils.Result;
import com.leyou.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 判断数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data,@PathVariable("type")Integer type){
        Boolean bool = this.userService.checkUser(data, type);
        if (bool == null){
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bool);
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public Result sendVerifyCode(@PathParam("phone")String phone){
        if (phone == null){
            return ResultUtil.fail(ExceptionEnum.PHONE_CANNOT_BE_NULL);
        }
        String code = this.userService.sendVerfyCode(phone);
        return ResultUtil.succ(code);
    }

    /**
     * 用户注册接口
     * @param username
     * @param phone
     * @param password
     * @param code
     * @return
     */
    @PostMapping("register")
    public Result register(@Valid String username, @Valid String phone,@Valid String password,String code){
        if (username ==null || phone == null || password == null || code == null){
            return ResultUtil.fail(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        String result = this.userService.register(username,phone,password, code);
        if ("0001".equals(result)){
           return ResultUtil.fail(ExceptionEnum.INVALID_VERIFY_CODE);
        }
        if ("0002".equals(result)){
            return ResultUtil.fail(ExceptionEnum.ALREADY_USERNAME_PASSWORD);
        }
        return ResultUtil.succ();

    }
}