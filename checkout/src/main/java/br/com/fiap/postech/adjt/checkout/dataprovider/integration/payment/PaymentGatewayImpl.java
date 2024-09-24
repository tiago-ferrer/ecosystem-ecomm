package br.com.fiap.postech.adjt.checkout.dataprovider.integration.payment;

import br.com.fiap.postech.adjt.checkout.application.dto.FieldDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentCheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentResponseDTO;
import br.com.fiap.postech.adjt.checkout.application.mappers.CheckoutMapper;
import br.com.fiap.postech.adjt.checkout.dataprovider.integration.dto.CartByConsumerDTO;
import br.com.fiap.postech.adjt.checkout.domain.gateway.PaymentGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderStatusModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentGatewayImpl implements PaymentGateway {

//    @Value("${app.payment-base-url}")
//    private String paymentBaseUrl;

    private final WebClient webClientPayment;

    private final CheckoutMapper checkoutMapper;

    private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayImpl.class);

    @Override
    public Mono<OrderStatusModel> processPayment(CheckoutModel checkoutModel, UUID orderId) {
        PaymentCheckoutDTO paymentCheckoutDTO = buildPayloadPaymentCheckout(checkoutModel, orderId);
        return this.webClientPayment
                .method(HttpMethod.POST)
                .uri("/create-payment")
                .bodyValue(paymentCheckoutDTO)
                .retrieve()
                .bodyToMono(OrderStatusModel.class);
    }

    private PaymentCheckoutDTO buildPayloadPaymentCheckout(CheckoutModel checkoutModel, UUID orderId) {
        return new PaymentCheckoutDTO(
                orderId.toString(),
                checkoutModel.getAmount(),
                checkoutModel.getCurrency(),
                new PaymentDTO(
                        checkoutModel.getPaymentMethod().getType(),
                        new FieldDTO(
                                checkoutModel.getPaymentMethod().getFields().getNumber(),
                                checkoutModel.getPaymentMethod().getFields().getExpiration_month(),
                                checkoutModel.getPaymentMethod().getFields().getExpiration_year(),
                                checkoutModel.getPaymentMethod().getFields().getCvv(),
                                checkoutModel.getPaymentMethod().getFields().getName()
                        )
                )
        );
    }

}
