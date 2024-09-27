package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.cart.Item;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import br.com.fiap.postech.adjt.checkout.exception.OrderNotFoundException;
import br.com.fiap.postech.adjt.checkout.repository.CartRepository;
import br.com.fiap.postech.adjt.checkout.repository.ItemRepository;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.CartService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartService cartService;

    @Mock
    private ModelMapper mapper;


    @Test
    void testCreateOrderSuccess() {
        when(cartService.findByConsumerId(Mockito.any())).thenReturn(TestUtils.buildCartDto());
        when(orderRepository.save(Mockito.any())).thenReturn(TestUtils.buildOrder());
        when(mapper.map(Mockito.any(), Mockito.eq(Order.class))).thenReturn(TestUtils.buildOrder());
        Order order = orderService.createOrder(TestUtils.buildOrderRequestDto());
        assertNotNull(order);
        assertInstanceOf(Order.class, order);
        assertInstanceOf(UUID.class, order.getOrderId());
    }


    @Nested
    class TestsGetById {

        @Test
        void testGetByIdSuccess() {
            when(orderRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(TestUtils.buildOrder()));
            when(mapper.map(Mockito.any(), Mockito.eq(OrderResponseDto.class)))
                    .thenReturn(TestUtils.buildOrderResponseDto());
            OrderResponseDto orderResponseDto = orderService.getById(TestUtils.genUUID());
            assertNotNull(orderResponseDto);
            assertInstanceOf(OrderResponseDto.class, orderResponseDto);
        }

        @Test
        void testGetByIdWhenNotFound() {
            when(orderRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
            String msgError = assertThrows(OrderNotFoundException.class, () ->
                    orderService.getById(TestUtils.genUUID())).getMessage();
            assertEquals("OrderId not found.", msgError);
        }

    }


    @Nested
    class TestsUpdateStatusByStatusName {

        @Test
        void testUpdateStatusByStatusNameSuccess() {
            when(orderRepository.findById(Mockito.any())).thenReturn(Optional.of(TestUtils.buildOrder()));
            orderService.updateStatusByStatusName(TestUtils.genUUID(), PaymentStatus.pending);
            verify(orderRepository, times(1)).save(Mockito.any());
        }

        @Test
        void testUpdateStatusByStatusNameWhenNotFound() {
            when(orderRepository.findById(Mockito.any())).thenReturn(Optional.empty());
            String msgError = assertThrows(OrderNotFoundException.class, () ->
                    orderService.updateStatusByStatusName(TestUtils.genUUID(), PaymentStatus.pending)).getMessage();
            assertEquals("OrderId not found.", msgError);
        }

    }


    @Nested
    class TestsGetOrdersByConsumer {

        @Test
        void testGetOrdersByConsumer() {
            when(orderRepository.findById(Mockito.any())).thenReturn(Optional.of(TestUtils.buildOrder()));
            orderService.updateStatusByStatusName(TestUtils.genUUID(), PaymentStatus.pending);
            verify(orderRepository, times(1)).save(Mockito.any());
        }

        @Test
        void testUpdateStatusByStatusNameWhenNotFound() {
            when(orderRepository.findById(Mockito.any())).thenReturn(Optional.empty());
            String msgError = assertThrows(OrderNotFoundException.class, () ->
                    orderService.updateStatusByStatusName(TestUtils.genUUID(), PaymentStatus.pending)).getMessage();
            assertEquals("OrderId not found.", msgError);
        }

    }

    @Test
    void testGetOrdersByConsumer() {
        when(orderRepository.findByConsumerId(Mockito.any())).thenReturn(List.of(TestUtils.buildOrder()));
        List<Order> resp =  orderService.getOrdersByConsumer(TestUtils.genUUID());
        assertEquals(1, resp.size());
    }


    @Nested
    class TestsUpdateStatus {

        @Test
        void testGetOrdersByConsumer() {
            when(orderRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(TestUtils.buildOrder()));
            orderService.updateStatus(TestUtils.buildOrderResponseDto());
            verify(orderRepository, times(1)).save(Mockito.any());
        }

        @Test
        void testGetOrdersByConsumerWhenNotFound() {
            when(orderRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
            orderService.updateStatus(TestUtils.buildOrderResponseDto());
            verify(orderRepository, never()).save(Mockito.any());
        }

    }

    @Test
    void testConvertItemDto() {
        Item item = orderService.convertItemDto(TestUtils.buildItemDto());
        assertInstanceOf(Item.class, item);
    }



}
