package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderResponseDTO;
import br.com.fiap.postech.adjt.checkout.model.*;
import br.com.fiap.postech.adjt.checkout.repository.CheckoutRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CheckoutService {
    private final OrderService orderService;
    private final CheckoutRepository checkoutRepository;
    private final OrderService OrderService;
    private final CartService cartService;

    public CheckoutService(br.com.fiap.postech.adjt.checkout.service.OrderService orderService, CheckoutRepository checkoutRepository, br.com.fiap.postech.adjt.checkout.service.OrderService orderService1, CartService cartService) {
        this.orderService = orderService;
        this.checkoutRepository = checkoutRepository;
        OrderService = orderService1;
        this.cartService = cartService;
    }
    @Transactional
    public CheckoutResponseDTO processPayment(CheckoutRequestDTO checkoutRequestDTO) {
//        Cart cart = cartService.getCartDetails(checkoutRequestDTO.consumerId());
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
//        PaymentMethod paymentMethod = new PaymentMethod(PaymentMethodType.valueOf(checkoutRequestDTO.paymentMethod().type()),checkoutRequestDTO.paymentMethod().fields());
            checkout = new Checkout(null, UUID.fromString(checkoutRequestDTO.consumerId()), checkoutRequestDTO.amount(), Currency.valueOf(checkoutRequestDTO.currency()), null, PaymentStatus.PENDING);
            checkoutRepository.save(checkout);
            order = orderService.saveOrder(cart, checkout);
            checkout.setOrderId(order.getOrderId());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
//        chamada kafka aqui
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


}
