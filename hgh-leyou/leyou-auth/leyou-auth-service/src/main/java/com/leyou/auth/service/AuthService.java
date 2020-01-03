package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtConfig;
import com.leyou.common.dto.UserInfoDto;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RedisClient;
import com.leyou.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@EnableConfigurationProperties(JwtConfig.class)
public class AuthService {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private RedisClient redisClient;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public UserInfoDto login(String username, String password) throws Exception {
        UserInfoDto userInfoDto = new UserInfoDto();
        User user = userClient.queryUser(username, password);
        if (user == null){
            logger.info("用户"+username+"不存在");
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        String token = JwtUtils.generateToken(userInfo,jwtConfig.getPriKey(),jwtConfig.getExpire());
        redisClient.set("token_" + user.getId().toString(),token,30, TimeUnit.DAYS);
        userInfoDto.setUserId(user.getId());
        userInfoDto.setUsername(user.getUsername());
        userInfoDto.setPhone(user.getPhone());
        userInfoDto.setCreated(user.getCreated());
        userInfoDto.setToken(token);

        return userInfoDto;
    }
}
