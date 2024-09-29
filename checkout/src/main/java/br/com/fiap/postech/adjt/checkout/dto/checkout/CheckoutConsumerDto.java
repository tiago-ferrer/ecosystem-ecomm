package br.com.fiap.postech.adjt.checkout.dto.checkout;

import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutConsumerDto {
    private List<OrderResponseDto> orders;


}
