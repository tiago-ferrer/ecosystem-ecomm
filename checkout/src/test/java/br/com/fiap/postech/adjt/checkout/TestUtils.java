package br.com.fiap.postech.adjt.checkout;

import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.dto.cart.ItemDto;
import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.FieldDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.cart.Cart;
import br.com.fiap.postech.adjt.checkout.entity.cart.Item;
import br.com.fiap.postech.adjt.checkout.entity.checkout.Checkout;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import br.com.fiap.postech.adjt.checkout.entity.payment.Field;
import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;
import br.com.fiap.postech.adjt.checkout.entity.payment.PaymentConfirmed;

import java.util.*;

public class TestUtils {

    public static Cart buildCart() {
        Cart cart = new Cart();
        cart.setItems(List.of(buildItem()));
        return cart;
    }

    public static Item buildItem() {
        Item item = new Item();
        item.setQnt(1);
        item.setItemId(1L);
        item.setCartId(new Cart(buildCartDto(), null));
        return item;
    }

    public static CartDto buildCartDto() {
        CartDto cartDto = new CartDto();
        cartDto.setItems(List.of(buildItemDto()));
        return cartDto;
    }

    public static ItemDto buildItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(1L);
        itemDto.setQnt(1);
        return itemDto;
    }

    public static Map<String, Object> buildMapCartItems() {
        Map<String, Object> itemsMap = new HashMap<>();
        itemsMap.put("itemId", 1L);
        itemsMap.put("qnt", 10);
        List<Map<String, Object>> itemsMapList = new ArrayList<>();
        itemsMapList.add(itemsMap);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("items", itemsMapList);
        return responseBody;
    }

    public static CartRequest buildCartRequest() {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setConsumerId(genUUID().toString());
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(10);
        return cartRequest;
    }

    public static Order buildOrder() {
        Order order = new Order();
        order.setOrderId(genUUID());
        order.setConsumerId(genUUID());
        order.setPaymentType("br_credit_card");
        order.setPaymentStatus(PaymentStatus.pending);
        order.setValue(100);
        order.setCart(buildCart());
        return order;
    }

    public static CheckoutDto buildCheckoutDto() {
        CheckoutDto dto = new CheckoutDto();
        dto.setConsumerId(genUUID().toString());
        dto.setAmount(1000);
        dto.setCurrency("BRL");
        dto.setPayment_method(buildPaymentDto());
        dto.setPaymentType("br_credit_card");
        dto.setStatus(PaymentStatus.approved);
        return dto;
    }

    public static PaymentDto buildPaymentDto() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setFields(buildFieldDto());
        paymentDto.setType("br_credit_card");
        return paymentDto;
    }

    public static FieldDto buildFieldDto() {
        FieldDto fieldDto = new FieldDto();
        fieldDto.setCvv("789");
        fieldDto.setName("Josefino");
        fieldDto.setNumber("2111111111111111");
        fieldDto.setExpiration_month("12");
        fieldDto.setExpiration_year("27");
        return fieldDto;
    }

    public static Checkout buildCheckout() {
        Checkout checkout = new Checkout();
        checkout.setCheckoutId(genUUID());
        checkout.setAmount(1000);
        checkout.setCurrency("BRL");
        checkout.setPaymentType("br_credit_card");
        checkout.setConsumerId(genUUID());
        checkout.setValue(1000);
        checkout.setPaymentStatus(PaymentStatus.pending);
        checkout.setPaymentMethod(buildPayment());
        return checkout;
    }

    public static Payment buildPayment() {
        Payment payment = new Payment();
        payment.setField(buildField());
        payment.setId(1L);
        return payment;
    }

    public static Field buildField() {
        Field field = new Field();
        field.setCvv("789");
        field.setName("Josefino");
        field.setNumber("2111111111111111");
        field.setExpiration_month("12");
        field.setExpiration_year("27");
        return field;
    }

    public static OrderRequestDto buildOrderRequestDto() {
        OrderRequestDto dto = new OrderRequestDto();
        dto.setOrderId(genUUID().toString());
        dto.setValue(1000);
        dto.setConsumerId(genUUID().toString());
        dto.setPaymentType("paymentType");
        dto.setPaymentStatus(PaymentStatus.pending);
        return dto;
    }

    public static OrderResponseDto buildOrderResponseDto() {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(genUUID());
        dto.setPaymentType("paymentType");
        dto.setPaymentStatus(PaymentStatus.pending);
        dto.setValue(1000);
        dto.setItems(List.of(buildItemDto()));
        return dto;
    }

    public static PaymentRequestDto buildPaymentRequestDto() {
        PaymentRequestDto dto = new PaymentRequestDto();
        dto.setAmount(1000);
        dto.setCurrency("BRL");
        dto.setPayment_method(buildPaymentDto());
        dto.setOrderId(genUUID());
        return dto;
    }

    public static PaymentResponseDto buildPaymentResponseDto() {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setStatus(PaymentStatus.pending.toString());
        dto.setPaymentId(genUUID().toString());
        return dto;
    }

    public static PaymentConfirmed buildPaymentConfirmed() {
        PaymentConfirmed paymentConfirmed = new PaymentConfirmed();
        paymentConfirmed.setPaymentId(genUUID());
        paymentConfirmed.setStatus(PaymentStatus.approved);
        paymentConfirmed.setOrderId(genUUID());
        return paymentConfirmed;
    }

    public static UUID genUUID() {
        return UUID.fromString("69b4bfbc-5558-4d34-b920-bed1669a8a67");
    }

}
