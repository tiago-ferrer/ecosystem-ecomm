package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutConsumerDto;
import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutDto;
import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.FieldDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import br.com.fiap.postech.adjt.checkout.entity.payment.Field;
import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;
import br.com.fiap.postech.adjt.checkout.exception.CartConsumerException;
import br.com.fiap.postech.adjt.checkout.producer.PaymentProducer;
import br.com.fiap.postech.adjt.checkout.repository.CheckoutRepository;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutRepository repository;
    private final OrderService service;
    private final PaymentServiceImpl paymentService;
    private final PaymentProducer paymentProducer;
    private final ModelMapper mapper;


    @Autowired
    public CheckoutServiceImpl(CheckoutRepository repository,
                               OrderService service,
                               PaymentServiceImpl paymentService,
                               PaymentProducer paymentProducer,
                               ModelMapper mapper) {
        this.repository = repository;
        this.service = service;
        this.paymentService = paymentService;
        this.paymentProducer = paymentProducer;
        this.mapper = mapper;
    }

    @Override
    public CheckoutResponse createChekout(CheckoutDto checkout) {

        FieldDto fieldDto = new FieldDto(checkout.getPayment_method().getFields().getNumber(),
                checkout.getPayment_method().getFields().getExpiration_month(),
                checkout.getPayment_method().getFields().getExpiration_year(),
                checkout.getPayment_method().getFields().getCvv(),
                checkout.getPayment_method().getFields().getName());

        Field field = fieldDto.convertDto(fieldDto);
        PaymentDto paymentDto = new PaymentDto(checkout.getPayment_method().getType(), fieldDto);
        Payment payment = mapper.map(paymentDto, Payment.class);
        payment.setField(field);
        payment = paymentService.createPayment(payment);

        OrderRequestDto orderRequestDto = new OrderRequestDto(
                checkout.getConsumerId().toString(),
                checkout.getPayment_method().getType(),
                checkout.getAmount(),
                PaymentStatus.pending);

        Order order = service.createOrder(orderRequestDto);

        var newCheckout = checkout.convertFromDto(checkout);
        newCheckout.setPaymentStatus(PaymentStatus.pending);
        newCheckout.setPaymentType(payment.getType());
        newCheckout.setPaymentMethod(payment);
        repository.save(newCheckout);

        PaymentRequestDto paymentRequest = new PaymentRequestDto(order.getOrderId(),
                            checkout.getAmount(),
                            checkout.getCurrency(),
                            paymentDto);

        paymentProducer.send(paymentRequest);

        return new CheckoutResponse(paymentRequest);

    }

    public CheckoutConsumerDto getOrdersByConsumer(String consumerId){
        CheckoutConsumerDto checkoutConsumerDto = new CheckoutConsumerDto();
        if(isValidUUID(consumerId)) {
            List<Order> orderList = service.getOrdersByConsumer(UUID.fromString(consumerId));
            List<OrderResponseDto> ordersDto = orderList.stream().map(OrderResponseDto::new).toList();
            checkoutConsumerDto.setOrders(ordersDto);
            return checkoutConsumerDto;
        } else
            throw new CartConsumerException("Invalid consumerId Format");
    }

    public boolean isValidUUID(String uuid) {
        return Pattern.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", uuid);
    }
}
