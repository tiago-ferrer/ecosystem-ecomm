package br.com.fiap.postech.adjt.checkout.application.mappers;

import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderItemModel;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {

    @InjectMocks
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toOrderDTO_ShouldReturnOrderDTO_WhenGivenOrderModel() {
        // Arrange
        OrderItemModel orderItemModel1 = new OrderItemModel(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), 2L, "itemCode");
        OrderItemModel orderItemModel2 = new OrderItemModel(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), 3L, "itemCode2");
        List<OrderItemModel> orderItemModels = Arrays.asList(orderItemModel1, orderItemModel2);
        OrderModel orderModel = new OrderModel(UUID.fromString("281937e1-349e-4d3c-81e1-f88953f54c97"), PaymentStatus.denied, "consumerId", "CREDIT", 50.0, orderItemModels);

        // Act
        OrderDTO mappedOrderDTO = orderMapper.toOrderDTO(orderModel);

        // Assert
        assertEquals("281937e1-349e-4d3c-81e1-f88953f54c97", mappedOrderDTO.orderId());
    }
}