package br.com.fiap.postech.adjt.checkout.listener;

import br.com.fiap.postech.adjt.checkout.model.Checkout;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CheckoutListener {

    //Test para validar o consumo da mensagem enviada para o tópico
    @KafkaListener(topics = "checkout-processing-topic", groupId = "checkout-group")
    public void listen(Checkout checkout) {
        System.out.println("Processing checkout: " + checkout);
    }

}
