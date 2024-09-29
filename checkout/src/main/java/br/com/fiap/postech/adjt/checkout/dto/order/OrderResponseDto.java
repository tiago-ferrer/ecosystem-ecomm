package br.com.fiap.postech.adjt.checkout.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.ItemDto;
import br.com.fiap.postech.adjt.checkout.entity.cart.Item;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    @NotBlank
    private UUID orderId;
    private List<ItemDto> items;
    private String paymentType;
    private int value;
    private PaymentStatus paymentStatus;

    public OrderResponseDto convertToDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getOrderId());
        dto.setPaymentType(order.getPaymentType());
        dto.setValue(order.getValue());
        dto.setPaymentStatus(order.getPaymentStatus());
        List<ItemDto> itemsDto = order.getCart().getItems().stream().map(this::convertToItemDto).toList();
        dto.setItems(itemsDto);
        return dto;
    }

    public OrderResponseDto() {}

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.paymentType = order.getPaymentType();
        this.value = order.getValue();
        this.paymentStatus = order.getPaymentStatus();
        this.items = order.getCart().getItems().stream().map(this::convertToItemDto).toList();
    }

    public ItemDto convertToItemDto(Item item) {
        return new ItemDto(item);
    }

}
