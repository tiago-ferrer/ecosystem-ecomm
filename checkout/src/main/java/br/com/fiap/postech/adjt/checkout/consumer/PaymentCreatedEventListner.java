package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.payment.PaymentConfirmed;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import br.com.fiap.postech.adjt.checkout.service.impl.PaymentConfirmedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class  PaymentCreatedEventListner implements Consumer<PaymentRequestDto> {

    private final OrderService service;
    private final PaymentServiceAPI paymentService;
    private final ModelMapper mapper;
    private final PaymentConfirmedService paymentConfirmedService;

    @Override
    public void accept(PaymentRequestDto paymentRequestDto) {
        paymentService.processPayment(paymentRequestDto)
                .subscribe(paymentResponseDto -> {
                    log.info("Resposta da API externa recebida: " + paymentResponseDto.getPaymentId());
                    OrderResponseDto orderDto = service.getById(paymentRequestDto.getOrderId());


                    if (paymentResponseDto.getStatus().equalsIgnoreCase("APPROVED")) {
                        service.updateStatusByStatusName(paymentRequestDto.getOrderId(), PaymentStatus.approved);
                        orderDto.setPaymentStatus(PaymentStatus.approved);
                        log.info("Pedido aprovado: " + paymentResponseDto);
                    } else if (paymentResponseDto.getStatus().equalsIgnoreCase("DECLINED")) {
                        service.updateStatusByStatusName(paymentRequestDto.getOrderId(), PaymentStatus.declined);
                        orderDto.setPaymentStatus(PaymentStatus.declined);
                        log.info("Pedido cancelado: " + paymentResponseDto);
                    } else {
                        throw new InvalidOrderIdException("Formato inválido de orderId.");
                    }

                    PaymentConfirmed paymentConfirmed = new PaymentConfirmed(UUID.fromString(paymentResponseDto.getPaymentId()), orderDto.getOrderId(), orderDto.getPaymentStatus());
                    paymentConfirmedService.cadastrar(paymentConfirmed);
                }, error -> log.error("Erro no processamento do pagamento: " + error.getMessage()));
    }

}
