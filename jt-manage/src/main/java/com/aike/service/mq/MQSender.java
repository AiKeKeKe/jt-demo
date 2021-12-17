package com.aike.service.mq;

import com.aike.config.RabbitConfig;
import com.aike.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(Object obj) {
        try {
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_USER, ObjectMapperUtil.toJSON(obj), message -> {
                message.getMessageProperties().setCorrelationId(correlationData.getId());
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);//消息持久化
                message.getMessageProperties().setExpiration(String.valueOf(TimeUnit.DAYS.toMillis(1)));//消息ttl为24小时
                return message;
            });
        } catch (AmqpException e) {
            log.error("send fail", e);
        }
    }

}
