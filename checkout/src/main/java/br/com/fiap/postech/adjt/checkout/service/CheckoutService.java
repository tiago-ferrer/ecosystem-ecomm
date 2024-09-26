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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
    private final OrderService orderService;

    private final CheckoutRepository checkoutRepository;

    private final CartService cartService;

    private final KafkaTemplate<String, Checkout> checkoutKafkaTemplate;

    public CheckoutService(OrderService orderService,
                           CheckoutRepository checkoutRepository,
                           CartService cartService,
                           KafkaTemplate<String, Checkout> checkoutKafkaTemplate) {
        this.orderService = orderService;
        this.checkoutRepository = checkoutRepository;
        this.cartService = cartService;
        this.checkoutKafkaTemplate = checkoutKafkaTemplate;
    }

    @Transactional
    public CheckoutResponseDTO processPayment(CheckoutRequestDTO checkoutRequestDTO) {
//        Cart cart = cartService.getCartDetails(checkoutRequestDTO.consumerId());
//        validateCartItemList(cart);
        validatePaymentMethod(checkoutRequestDTO);
        Checkout checkout = null;
        Order order = null;
        try {
            Cart cart = new Cart();
            cart.setConsumerId(UUID.fromString("153e23c8-322e-4fec-b9c4-72b8f74ad002"));
            List<Item> itemList = new ArrayList<>();
            Item item1 = new Item();
            Item item2 = new Item();
            Item item3 = new Item();
            item1.setItemId(1L);
            item1.setPrice(25.00);
            item1.setQuantity(2);
            itemList.add(item1);
            item2.setItemId(3L);
            item2.setPrice(100.00);
            item2.setQuantity(1);
            itemList.add(item2);
            item3.setItemId(2L);
            item3.setPrice(50.00);
            item3.setQuantity(45);
            itemList.add(item3);
            cart.setItemList(itemList);

            checkout = new Checkout(UUID.fromString(checkoutRequestDTO.consumerId()), null, checkoutRequestDTO.amount(),
                    Currency.valueOf(checkoutRequestDTO.currency()),
                    PaymentMethodMapper.toEntity(checkoutRequestDTO.paymentMethod()), PaymentStatus.PENDING);
            checkoutRepository.saveAndFlush(checkout);
            order = orderService.saveOrder(cart, checkout);
            checkout.setOrderId(order.getOrderId());

            //Envio da mensagem para o tópico checkout-processing-topic
            try {
                checkoutKafkaTemplate.send("checkout-processing-topic", checkout.getOrderId().toString(), checkout);
                System.out.println("Send Checkout: " + checkout);
            } catch (Exception e) {
                throw new RuntimeException("Error while sending message to Kafka", e);
            }

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
