package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Length(min = 3,max = 8,message = "用户名需要在3到8位之间")
    private String username;

    /**
     * 密码
     */
    @Length(min = 8,max = 16,message = "密码需要在8到16位之间")
    @JsonIgnore//对象序列化为Json字符串时忽略该属性
    private String password;

    /**
     *手机号
     */
    @Pattern(regexp = "^1[34578]\\d{9}$",message = "手机号不合法")
    private String phone;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 密码的盐值
     */
    @JsonIgnore
    private String salt;




}
