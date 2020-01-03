package com.leyou.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;


@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

   // @Value("${jwt.secret}")
    private String secret;

   // @Value("${jwt.pubKey}")
    private String pubKey;

   // @Value("${jwt.priKey}")
    private String priKey;

    //@Value("${jwt.expire}")
    private int expire;

   // @Value("${jwt.cookieName}")
    private String cookieName;



}
