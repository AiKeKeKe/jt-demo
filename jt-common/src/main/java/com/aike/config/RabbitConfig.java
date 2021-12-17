package com.aike.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "jt.rabbitmq.enable", havingValue = "true")
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    /** 消息队列 */
    public static final String QUEUE = "queue";

    /** 直连交换机 */
    public static final String EXCHANGE = "exchange_direct";

    /** 直连路由器的routing-key */
    public static final String ROUTING_KEY = "jt_event_key";

    /** 用户消息队列 */
    public static final String QUEUE_USER = "queue_user";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(host + ":" + port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setRequestedHeartBeat(0);
        log.info("Rabbit : " + host + " | " + port + " | " + username + " | " + virtualHost);
        return connectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> listenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMessageConverter(messageConverter());
        containerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return containerFactory;
    }

    @Bean
    public Queue getQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange getDirectExchange() {
        // 配置直连路由器
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(getQueue()).to(getDirectExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Queue userQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", TimeUnit.DAYS.toMillis(1));
        return new Queue(QUEUE_USER, true, false, false, arguments);
    }
}
