package com.leyou.auth.controller;

import com.leyou.auth.config.JwtConfig;
import com.leyou.auth.service.AuthService;
import com.leyou.common.dto.UserInfoDto;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.utils.CookieUtils;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "用户授权中心接口")
@EnableConfigurationProperties(JwtConfig.class)
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 用户登录接口
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "用户登录接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "username",value = "用户名",required = true,dataType = "String"),
    @ApiImplicitParam(name = "password",value = "密码",required = true,dataType = "String")
    })
    @PostMapping("login")
    public Result accredit(String username, String password, HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            ResultUtil.fail(ExceptionEnum.CREATE_TOKEN_ERROR);
            logger.info("用户名或密码为空");
        }
        UserInfoDto userInfoDto = null;
        try {
            userInfoDto = authService.login(username,password);
        } catch (Exception e) {
            logger.info("登录错误");
            e.printStackTrace();
        }
        if (userInfoDto == null){
            logger.info("用户名或密码错误");
            return ResultUtil.fail(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        if (StringUtils.isBlank(userInfoDto.getToken())){
            logger.info(username+" 生成token失败");
            ResultUtil.fail(ExceptionEnum.CREATE_TOKEN_ERROR);
        }
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),userInfoDto.getToken(),jwtConfig.getExpire() * 60);
        return ResultUtil.succ(userInfoDto);

    }
}


