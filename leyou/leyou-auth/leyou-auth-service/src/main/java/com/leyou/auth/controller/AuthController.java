package com.leyou.auth.controller;

import com.leyou.auth.config.JwtConfig;
import com.leyou.auth.service.AuthService;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.Result;
import com.leyou.common.utils.ResultUtil;
import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtConfig.class)
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("accredit")
    public Result accredit(String username, String password, HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            ResultUtil.fail(ExceptionEnum.CREATE_TOKEN_ERROR);
            logger.info("用户名或密码为空");
        }
        String token = authService.accredit(username,password);
        if (StringUtils.isBlank(token)){
            logger.info(username+" 生成token失败");
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),token,jwtConfig.getExpire() * 60);
        return ResultUtil.succ(token);

    }
}


