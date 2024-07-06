package com.vn.projectmanagement.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue}")
    String queueName;

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    /**
     * Tạo queue với tên queueName
     *
     * @return Queue
     */
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    /**
     * Direct exchange là một trong 4 loại exchange trong RabbitMQ (Direct, Fanout, Topic, Headers).
     * Direct exchange sẽ định tuyến message đến queue có cùng routing key với routing key của message.
     *
     * @return DirectExchange
     */
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    /**
     * Binding queue với exchange với routing key
     * Binding sẽ định tuyến message từ exchange đến queue
     *
     * @param queue    queue cần binding
     * @param exchange exchange cần binding
     * @return Binding
     */
    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    /**
     * MessageConverter sẽ chuyển đổi message từ Object sang JSON và ngược lại
     * Jackson2JsonMessageConverter sử dụng ObjectMapper để chuyển đổi message
     *
     * @return MessageConverter object to JSON
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate sẽ gửi message đến exchange với routing key tương ứng với queue cần nhận message
     * MessageConverter sẽ chuyển đổi message từ Object sang JSON
     *
     * @param connectionFactory connectionFactory
     * @return AmqpTemplate
     */
    @Bean
    public AmqpTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
