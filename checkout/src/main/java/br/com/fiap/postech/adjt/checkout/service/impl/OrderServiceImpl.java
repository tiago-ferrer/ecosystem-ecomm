package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.dto.cart.ItemDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.cart.Cart;
import br.com.fiap.postech.adjt.checkout.entity.cart.Item;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import br.com.fiap.postech.adjt.checkout.exception.OrderNotFoundException;
import br.com.fiap.postech.adjt.checkout.repository.CartRepository;
import br.com.fiap.postech.adjt.checkout.repository.ItemRepository;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.CartService;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;

    private ModelMapper mapper;
    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            ItemRepository itemRepository,
                            CartService cartService,
                            ModelMapper mapper) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.cartService = cartService;
        this.mapper = mapper;
    }

    /**
     * Método createOrder: Persiste o pedido.
     * Obtem o carrinho de um consumidor e persiste um objeto order
     * */

    @Override
    public Order createOrder(OrderRequestDto orderDto) {
        UUID consumerId = UUID.fromString(orderDto.getConsumerId());
        CartRequest cartRequest = new CartRequest();
        cartRequest.setConsumerId(orderDto.getConsumerId());
        List<Item> items = new ArrayList<>();
        CartDto cartDto = (cartService.findByConsumerId(cartRequest));
        Cart cart = new Cart(cartDto, consumerId);
        cartRepository.save(cart);
        cart.getItems().forEach(cartLocate -> cartLocate.setCartId(new Cart(cart.getCartId())));
        itemRepository.saveAll(cart.getItems());
        orderDto.setPaymentStatus(PaymentStatus.pending);
        Order order = mapper.map(orderDto, Order.class);
        order.setCart(cart);
        order = orderRepository.save(order);
        cartService.deleteByConsumerId(cartRequest);
        return order;
    }

    @Override
    public OrderResponseDto getById(UUID id) {
        var order = orderRepository.findById(id).orElseThrow(()-> new OrderNotFoundException("OrderId not found."));
        return mapper.map(order, OrderResponseDto.class);
    }

    @Override
    public OrderResponseDto retrieveById(UUID id) {
        var order = orderRepository.findByOrderId(id).orElseThrow(()-> new OrderNotFoundException("OrderId not found."));
        CartDto cartDto = mapper.map(order.getCart(), CartDto.class);
        var orderResponse =  mapper.map(order, OrderResponseDto.class);
        orderResponse.setItems(cartDto.getItems());
        return orderResponse;
    }

    public void updateStatusByStatusName(UUID orderId, PaymentStatus paymentStatus){
        var order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException("OrderId not found."));
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
    }
    @Override
    public List<Order> getOrdersByConsumer(UUID consumerId) {
        return orderRepository.findByConsumerId(consumerId);
    }

    @Override
    public void updateStatus(OrderResponseDto order) {
        var existe = orderRepository.findById(order.getOrderId());
        if(existe.isPresent()){
                var newOrder = existe.get();
                newOrder.setPaymentStatus(order.getPaymentStatus());
                orderRepository.save(newOrder);
        }

    }

    protected Item convertItemDto(ItemDto dto){
        Item item = new Item();
        item.setItemId(dto.getItemId());
        item.setQnt(dto.getQnt());
        return item;
    }

}
