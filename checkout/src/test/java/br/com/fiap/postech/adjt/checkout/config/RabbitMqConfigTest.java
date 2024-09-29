package br.com.fiap.postech.adjt.checkout.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;


import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class RabbitMqConfigTest {

    @InjectMocks
    private RabbitMqConfig rabbitMqConfig;

    @Mock
    private ConnectionFactory connectionFactory;


    @Test
    void testPaymentRequestsQueue() {
        assertInstanceOf(Queue.class, rabbitMqConfig.paymentRequestsQueue());
    }

    @Test
    void testPaymentResponsesQueue() {
        assertInstanceOf(Queue.class, rabbitMqConfig.paymentResponsesQueue());
    }

    @Test
    void testProducerJackson2MessageConverter() {
        assertInstanceOf(Jackson2JsonMessageConverter.class,
                rabbitMqConfig.producerJackson2MessageConverter());
    }

    @Test
    void testRabbitTemplate() {
        assertInstanceOf(RabbitTemplate.class,
                rabbitMqConfig.rabbitTemplate(connectionFactory));
    }

}
