package com.leyou.sms.mq;

import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.sms.queue",durable = "true"),
            exchange = @Exchange(value = "leyou.sms.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"verifycode.sms"}
    ))
    public void sendSms(Map<String,String> msg){

        try {
            if (CollectionUtils.isEmpty(msg)){
                return;
            }
            String phone = msg.get("phone");
            String code = msg.get("code");
            if (StringUtils.isNoneBlank(phone) && StringUtils.isNoneBlank(code)){
                this.smsUtil.sendSms(phone,code,this.smsProperties.getSignName(),this.smsProperties.getVerifyCodeTemplate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
