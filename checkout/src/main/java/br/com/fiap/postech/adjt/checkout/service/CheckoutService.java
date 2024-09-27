package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.exception.EmptyCartException;
import br.com.fiap.postech.adjt.checkout.exception.InvalidPaymentMethodException;
import br.com.fiap.postech.adjt.checkout.exception.PaymentProcessingException;
import br.com.fiap.postech.adjt.checkout.kafka.PaymentProducer;
import br.com.fiap.postech.adjt.checkout.mapper.PaymentMethodMapper;
import br.com.fiap.postech.adjt.checkout.model.*;
import br.com.fiap.postech.adjt.checkout.repository.CheckoutRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

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
        Mono<Cart> monoCart = cartService.getCart(UUID.fromString(checkoutRequestDTO.consumerId()));
        Cart cart = monoCart.block();
        try {
            validateCartItemList(cart);
            validatePaymentMethod(checkoutRequestDTO);
        } catch (EmptyCartException e) {
            throw new EmptyCartException();
        } catch (InvalidPaymentMethodException e) {
            throw new InvalidPaymentMethodException();
        }
        Checkout checkout = new Checkout(UUID.fromString(checkoutRequestDTO.consumerId()), null, checkoutRequestDTO.amount(),
                Currency.valueOf(checkoutRequestDTO.currency()),
                PaymentMethodMapper.toEntity(checkoutRequestDTO.paymentMethod()), PaymentStatus.pending);
        checkoutRepository.save(checkout);
        Order order = orderService.createAndSaveOrder(cart, checkout);
        checkout.setOrderId(order.getOrderId());
        try {
            paymentProducer.sendPaymentRequest(order, checkout);
        } catch (Exception e) {
            throw new PaymentProcessingException();
        }
        return new CheckoutResponseDTO(order.getOrderId().toString(), checkout.getStatus());
    }


    public Checkout findByOrderId(UUID orderId) {
        return checkoutRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Checkout not found for order: " + orderId));
    }

    private void validatePaymentMethod(CheckoutRequestDTO checkoutRequestDTO) {
        PaymentMethod paymentMethod = PaymentMethodMapper.toEntity(checkoutRequestDTO.paymentMethod());
        if (paymentMethod == null || !areFieldsValid(paymentMethod)) {
            throw new InvalidPaymentMethodException();
        }
    }

    private boolean areFieldsValid(PaymentMethod paymentMethod) {
        System.out.println(paymentMethod.getType().toString());
        if (paymentMethod.getType().equals(PaymentMethodType.br_credit_card) ||
                paymentMethod.getType().equals(PaymentMethodType.br_debit_card)) {
            return true;
        }

        PaymentMethodFields fields = paymentMethod.getFields();

        String expirationYear = fields.getExpiration_year();
        System.out.println(expirationYear);
        if (expirationYear != null && !expirationYear.isEmpty()) {
            int year = Integer.parseInt(expirationYear);
            if (year >= 2024) {
                return true;
            }
        }

        String expirationMonth = fields.getExpiration_month();
        System.out.println(expirationMonth);
        if (expirationMonth != null && !expirationMonth.isEmpty()) {
            int month = Integer.parseInt(expirationMonth);
            if (month > 0 && month <= 12) {
                return true;
            }
        }

        String cardNumber = fields.getNumber();
        System.out.println(cardNumber);
        if (cardNumber != null && !cardNumber.isEmpty()) {
            int numberLength = cardNumber.length();
            if (numberLength >= 13 && numberLength <= 16) {
                return true;
            }
        }
        return false;
    }

    private static void validateCartItemList(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }
    }

}
