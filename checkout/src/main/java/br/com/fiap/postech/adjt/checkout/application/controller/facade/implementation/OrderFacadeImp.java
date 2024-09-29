package br.com.fiap.postech.adjt.checkout.application.controller.facade.implementation;

import br.com.fiap.postech.adjt.checkout.application.controller.facade.OrderFacade;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.application.mappers.CheckoutMapper;
import br.com.fiap.postech.adjt.checkout.application.mappers.OrderMapper;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import br.com.fiap.postech.adjt.checkout.domain.usecase.CreateOrderUseCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFacadeImp implements OrderFacade {

    private final OrderMapper orderMapper;
    private final CreateOrderUseCase orderUseCase;
    private final CheckoutMapper checkoutMapper;

    public OrderFacadeImp(OrderMapper orderMapper, CreateOrderUseCase orderUseCase, CheckoutMapper checkoutMapper) {
        this.orderMapper = orderMapper;
        this.orderUseCase = orderUseCase;
        this.checkoutMapper = checkoutMapper;
    }

    @Override
    public CheckoutResponseDTO createOrder(CheckoutDTO checkoutDTO) throws AppException {
        CheckoutModel checkoutModel = checkoutMapper.toCheckoutModel(checkoutDTO);
        OrderModel order = orderUseCase.createOrder(checkoutModel);
        return new CheckoutResponseDTO(order.getOrderId().toString(),
                order.getPaymentStatus().name());
    }

    @Override
    public OrderDTO getOrderById(String orderId) throws AppException {
        return orderMapper.toOrderDTO(orderUseCase.getOrderById(orderId));
    }

    @Override
    public List<OrderDTO> getOrderByCustomerId(String customerId) throws AppException {
        List<OrderModel> result = orderUseCase.getOrderByConsumerId(customerId);

        if(result.isEmpty()) {
            throw new AppException("Cart is empty");
        }

        return result.stream().map(orderMapper::toOrderDTO).toList();

    }
}
