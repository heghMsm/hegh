package com.leyou.user.service;

import com.leyou.common.utils.RedisClient;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.util.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify";

    /**
     * 判断数据是否可用
     * @param data
     * @param type
     * @return
     */
    public Boolean checkUser(String data, Integer type) {

        User record = User.builder().build();
        //判断数据类型
        if (type == 1){
            record.setUsername(data);
        }else if (type == 2){
            record.setPhone(data);
        }else {
            return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    public String sendVerfyCode(String phone) {
        // 生成key
        String key = KEY_PREFIX + phone;
        if (StringUtils.isBlank(phone)){
            return null;
        }
        //生成短信验证码
        String code = NumberUtils.generateCode(6);
        //发送消息到RabbitMQ
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        this.amqpTemplate.convertAndSend("leyou.sms.exchange","verifycode.sms",msg);
        //将验证码保存到Redis
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        return code;
    }

    /**
     * 用户注册接口
     * @param username
     * @param phone
     * @param password
     * @param code
     * @return
     */
    public String register(String username, String phone,String password,String code) {
        //查询Redis中验证码
        String redisCode = this.redisClient.get(KEY_PREFIX + phone);
        //校验验证码
        if (!code.equals(redisCode)){
            return "0001";
        }
        List<User> users = this.userMapper.selectAll();
       for (User oneUser: users) {
            if (username.equals(oneUser.getUsername()) || phone.equals(oneUser.getPhone()))
               return "0002";
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        //加盐加密
        String md5Password = CodecUtils.md5Hex(password, salt);
        //新增用户
        User user = User.builder().username(username).phone(phone).password(md5Password).salt(salt).created(new Date()).build();
        this.userMapper.insertSelective(user);
        this.redisTemplate.delete(KEY_PREFIX + phone);
        return "0000";
    }
}
