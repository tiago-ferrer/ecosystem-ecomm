package br.com.fiap.postech.adjt.checkout.dataprovider.broker.kafka;

import br.com.fiap.postech.adjt.checkout.domain.gateway.PaymentGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentGatewayImpl implements PaymentGateway {
    @Override
    public void sendPayment(OrderModel orderModel) {
        /*
            TODO implementar integracao kafka para enviar pagamento
            para broker
         */
        log.info("enviar mensagem de pagamento do pedido: {} ", orderModel.getOrderId());
    }
}
