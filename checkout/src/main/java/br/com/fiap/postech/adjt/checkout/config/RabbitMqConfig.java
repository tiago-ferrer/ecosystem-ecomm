package br.com.fiap.postech.adjt.checkout.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue paymentRequestsQueue() {
        return new Queue("payment.requests", true);
    }

    @Bean
    public Queue paymentResponsesQueue() {
        return new Queue("payment.responses", true);
    }



    @Bean
    public Queue queuePaymentTest() {
        return new Queue("payment.queue", true);
    }


    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter((MessageConverter) producerJackson2MessageConverter());
        return rabbitTemplate;
    }


}
