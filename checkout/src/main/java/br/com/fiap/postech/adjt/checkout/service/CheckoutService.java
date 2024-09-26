package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderResponseDTO;
import br.com.fiap.postech.adjt.checkout.exception.EmptyCartException;
import br.com.fiap.postech.adjt.checkout.exception.InvalidPaymentMethodException;
import br.com.fiap.postech.adjt.checkout.mapper.PaymentMethodMapper;
import br.com.fiap.postech.adjt.checkout.model.*;
import br.com.fiap.postech.adjt.checkout.repository.CheckoutRepository;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
    private final OrderService orderService;

    private final CheckoutRepository checkoutRepository;

    private final CartService cartService;


    private final PaymentProducer paymentProducer;

    public CheckoutService(OrderService orderService,
                           CheckoutRepository checkoutRepository,
                           CartService cartService, PaymentProducer paymentProducer) {
        this.orderService = orderService;
        this.checkoutRepository = checkoutRepository;
        this.cartService = cartService;
        this.paymentProducer = paymentProducer;
    }

    @Transactional
    public CheckoutResponseDTO processPayment(CheckoutRequestDTO checkoutRequestDTO) {
        Cart cart = cartService.getCart(UUID.fromString(checkoutRequestDTO.consumerId()));
        validateCartItemList(cart);
        validatePaymentMethod(checkoutRequestDTO);
        Checkout checkout = null;
        Order order = null;

        try {
            checkout = new Checkout(UUID.fromString(checkoutRequestDTO.consumerId()), null, checkoutRequestDTO.amount(),
                    Currency.valueOf(checkoutRequestDTO.currency()),
                    PaymentMethodMapper.toEntity(checkoutRequestDTO.paymentMethod()), PaymentStatus.pending);
            checkoutRepository.saveAndFlush(checkout);
            order = orderService.createAndSaveOrder(cart, checkout);
            checkout.setOrderId(order.getOrderId());
            paymentProducer.sendPaymentRequest(order,checkout);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return new CheckoutResponseDTO(order.getOrderId().toString(), checkout.getStatus());
    }

    public OrderResponseDTO searchPaymentByOrderId(String orderId) {
        Order order = orderService.getOrderByOrderId(orderId);
        return order.toDTO();
    }

    public List<OrderResponseDTO> searchPaymentByConsumer(String consumerId) {
        List<Order> orders = orderService.getOrderByConsumerId(consumerId);
        List<OrderResponseDTO> ordersDto = orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
        return ordersDto;
    }

    private void validatePaymentMethod(CheckoutRequestDTO checkoutRequestDTO) {
        PaymentMethod paymentMethod = PaymentMethodMapper.toEntity(checkoutRequestDTO.paymentMethod());
        if (paymentMethod == null || !areFieldsValid(paymentMethod)) {
            throw new InvalidPaymentMethodException();
        }
    }

    private boolean areFieldsValid(PaymentMethod paymentMethod) {
        if (paymentMethod.getType().equals(PaymentMethodType.br_credit_card) ||
                paymentMethod.getType().equals(PaymentMethodType.br_debit_card)) {
            return true;
        }

        PaymentMethodFields fields = paymentMethod.getFields();

        String expirationYear = fields.getExpiration_year();
        if (expirationYear != null && !expirationYear.isEmpty()) {
            int year = Integer.parseInt(expirationYear);
            if (year >= 2024) {
                return true;
            }
        }

        String expirationMonth = fields.getExpiration_month();
        if (expirationMonth != null && !expirationMonth.isEmpty()) {
            int month = Integer.parseInt(expirationMonth);
            if (month > 0 && month <= 12) {
                return true;
            }
        }

        String cardNumber = fields.getNumber();
        if (cardNumber != null && !cardNumber.isEmpty()) {
            int numberLength = cardNumber.length();
            if (numberLength >= 13 && numberLength <= 16) {
                return true;
            }
        }
        return false;
    }

    private static void validateCartItemList(Cart cart) {
        if (cart.getItemList() == null || cart.getItemList().isEmpty()) {
            throw new EmptyCartException();
        }
    }

}
