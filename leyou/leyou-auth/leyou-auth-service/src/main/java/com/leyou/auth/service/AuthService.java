package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtConfig;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtConfig.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtConfig jwtConfig;

    public String accredit(String username, String password) {
        //根据用户名和密码查询
        User user = userClient.queryUser(username, password);
        if (user == null){
            return null;
        }
        //生成token
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        try {
           return JwtUtils.generateToken(userInfo,jwtConfig.getPriKey(),jwtConfig.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
