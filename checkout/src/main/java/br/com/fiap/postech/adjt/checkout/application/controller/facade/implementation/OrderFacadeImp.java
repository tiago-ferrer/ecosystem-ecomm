package br.com.fiap.postech.adjt.checkout.application.controller.facade.implementation;

import br.com.fiap.postech.adjt.checkout.application.controller.facade.OrderFacade;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.application.mappers.OrderMapper;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.usecase.OrderUseCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFacadeImp implements OrderFacade {

    private final OrderMapper orderMapper;
    private final OrderUseCase orderUseCase;

    public OrderFacadeImp(OrderMapper orderMapper, OrderUseCase orderUseCase) {
        this.orderMapper = orderMapper;
        this.orderUseCase = orderUseCase;
    }

    @Override
    public CheckoutResponseDTO createOrder(CheckoutDTO checkoutDTO) throws AppException {
        /*
        * Falta criar a lógica
        * */
        return null;
    }

    @Override
    public OrderDTO getOrderById(String orderId) throws AppException {
        return orderMapper.toOrderDTO(orderUseCase.getOrderById(orderId));
    }

    @Override
    public List<OrderDTO> getOrderByCustomerId(String customerId) throws AppException {
        return orderUseCase.getOrderByConsumerId(customerId).stream().map(orderMapper::toOrderDTO).toList();
    }
}
