package br.com.fiap.postech.adjt.checkout.dataprovider.integration.payment;

import br.com.fiap.postech.adjt.checkout.application.dto.FieldDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentCheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentResponseDTO;
import br.com.fiap.postech.adjt.checkout.domain.gateway.PaymentGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PaymentGatewayImpl implements PaymentGateway {

    @Value("${app.payment-base-url}")
    private String paymentBaseUrl;

    @Override
    public void getPayment(CheckoutModel checkoutModel, String orderId) {

        PaymentCheckoutDTO paymentCheckoutDTO = new PaymentCheckoutDTO(
                orderId,
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

        Mono<PaymentResponseDTO> response = getWebClient().post()
                .uri("/create-payment")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(paymentCheckoutDTO), PaymentResponseDTO.class)
                .retrieve()
                .bodyToMono(PaymentResponseDTO.class);

        response.subscribe(); //TODO A CONTINUAR A IMPLEMENTAÇÂO
    }

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(paymentBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }

}
