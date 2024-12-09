package br.com.fiap.postech.adjt.checkout.application.controller.facade;

import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;

import java.util.List;

public interface OrderFacade {

    CheckoutResponseDTO createOrder(CheckoutDTO checkoutDTO) throws AppException;

    OrderDTO getOrderById(String orderId) throws AppException;

    List<OrderDTO> getOrderByCustomerId(String customerId) throws AppException;

}
