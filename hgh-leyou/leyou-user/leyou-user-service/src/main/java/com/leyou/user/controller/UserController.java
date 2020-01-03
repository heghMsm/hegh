package com.leyou.user.controller;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.common.utils.Result;
import com.leyou.common.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@Api(value = "用户接口")
@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private UserService userService;

    /**
     * 判断数据是否可用
     * @param data
     * @param type
     * @return
     */
    @ApiOperation(value = "判断用户类型接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "data",value = "用户数据",required = true,dataType = "String"),
    @ApiImplicitParam(name = "type",value = "数据类型",required = true,dataType = "Integer")})
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
    @ApiOperation(value = "发送短信验证码接口")
    @ApiImplicitParam(name = "phone",value = "手机号",required = true,dataType ="String" )
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
    @ApiOperation(value = "用户注册接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "username",value = "用户名",required = true,dataType = "String"),
         @ApiImplicitParam(name = "password",value = "密码",required = true,dataType = "String"),
         @ApiImplicitParam(name ="phone",value ="手机号",required = true,dataType = "String"),
         @ApiImplicitParam(name = "code",value = "验证码",required = true,dataType = "String")})
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
        logger.info("用户;"+ username +"注册成功,验证码为：" + code);
        return ResultUtil.succ();
    }

    /**
     * 用户查询接口
     * @param username
     * @param password
     * @return
     */
    @ApiOperation(value = "用户查询接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "username",value = "用户名",required = true,dataType = "String"),
    @ApiImplicitParam(name = "password",value = "密码",required = true,dataType = "String")})
    @GetMapping("query")
    public User queryUser(@RequestParam("username")String username,@RequestParam("password")String password){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            logger.info("用户名或密码为空");
            return null;
        }
        User user = userService.login(username, password);

        if (user != null){
            return user;
        } else{
            logger.info("系统异常，用户" + username +"登录失败");
            return null;
        }
    }

}