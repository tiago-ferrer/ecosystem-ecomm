package br.com.fiap.postech.adjt.checkout.dataprovider.integration.payment;

import br.com.fiap.postech.adjt.checkout.application.dto.FieldDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentCheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentDTO;
import br.com.fiap.postech.adjt.checkout.application.exception.CustomException;
import br.com.fiap.postech.adjt.checkout.application.mappers.OrderMapper;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.domain.gateway.PaymentGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderStatusModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
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

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class PaymentGatewayImpl implements PaymentGateway {

    @Value("${app.payment-base-url}")
    private String paymentBaseUrl;

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Value("${app.payment-key}")
    private String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayImpl.class);

    public PaymentGatewayImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public void processPayment(CheckoutModel checkoutModel, UUID orderId) {
        PaymentCheckoutDTO paymentCheckoutDTO = buildPayloadPaymentCheckout(checkoutModel, orderId);
        Mono<OrderStatusModel> paymentObject =  webClientPayment()
                .method(HttpMethod.POST)
                .uri("/create-payment")
                .header("apiKey", apiKey)
                .bodyValue(paymentCheckoutDTO)
                .retrieve()
                .bodyToMono(OrderStatusModel.class);

        paymentObject
                .doOnSuccess(response -> {
                    updateOrder(response.getStatus(), orderId);
                })
                .doOnError(error -> {
                    throw new CustomException(error.getMessage());
                })
                .subscribe();

    }

    private void updateOrder(PaymentStatus status, UUID orderId) {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            OrderEntity newOrder = order.get();
            newOrder.setPaymentStatus(status);
            orderRepository.save(newOrder);
        }
    }

    private PaymentCheckoutDTO buildPayloadPaymentCheckout(CheckoutModel checkoutModel, UUID orderId) {
        return new PaymentCheckoutDTO(
                orderId.toString(),
                checkoutModel.getAmount().intValue(),
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

    private WebClient webClientPayment() {
        return WebClient.builder()
                .baseUrl(paymentBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
