package com.vn.projectmanagement.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnqueueDequeService {

    private final AmqpTemplate amqpTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    @Value("${rabbitmq.queue}")
    private String queue;

    /**
     * Constructor for EnqueueDequeService class with AmqpTemplate
     * Có vai trò như là một producer, gửi message đến exchange với routing key tương ứng
     * để định tuyến message đến queue cần thiết trong RabbitMQ
     *
     * @param amqpTemplate AmqpTemplate
     */
    public EnqueueDequeService(AmqpTemplate amqpTemplate, RabbitTemplate rabbitTemplate) {
        this.amqpTemplate = amqpTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }
    /**
     * Publish message to RabbitMQ
     * Gửi message đến exchange với routing key tương ứng để định tuyến message đến queue cần thiết trong RabbitMQ
     *
     * @param msg message
     */
    public void publishMessageToQueue(Object msg) {
        rabbitTemplate.convertAndSend(queue, msg);
    }

    /**
     * Listen to the queue and receive messages
     * @return the received message
     */
    public String listenToQueue() {
        Object message = rabbitTemplate.receiveAndConvert(queue);
        return message != null ? message.toString() : null;
    }
}
