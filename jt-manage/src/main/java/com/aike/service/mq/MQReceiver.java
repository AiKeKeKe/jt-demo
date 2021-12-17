package com.aike.service.mq;

import com.aike.config.RabbitConfig;
import com.aike.pojo.User;
import com.aike.util.ObjectMapperUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RabbitListener(queues = RabbitConfig.QUEUE_USER)
public class MQReceiver {

    @RabbitHandler
    public void process(String content, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String correlationId = message.getMessageProperties().getCorrelationId();
        try {
            User user = ObjectMapperUtil.toObject(content, User.class);
            log.info("[RabbitMQ] 收到消息, queue:{},tag:{},correlationId:{}", RabbitConfig.QUEUE_USER, deliveryTag, correlationId);
            // TODO: 2021/12/17  具体的业务逻辑

            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            log.error("[RabbitMQ] 确认消息抛出异常,e:{}", e.toString());
            // 重新确认
            try {
                Thread.sleep(50);
                channel.basicAck(deliveryTag, true);
            } catch (InterruptedException | IOException e1) {
                log.error("[RabbitMQ] 重新确认消息抛出异常,e:{}", e1.toString());
            }
        } catch (Exception e) {
            log.error("[RabbitMQ] 消费失败,e:{}", e.toString());
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException e1) {
                log.error("[RabbitMQ] 确认消费失败,e:{}", e1.toString());
            }
        }
    }
}
