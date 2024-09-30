//package br.com.fiap.postech.adjt.checkout.domain.entities.useCases;
//
//import br.com.fiap.postech.adjt.checkout.domain.entities.enums.OrderStatus;
//import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentConsumerPayload;
//import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentRequest;
//import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentResponse;
//import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.gateways.OrderGateway;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.coyote.BadRequestException;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@AllArgsConstructor
//@Slf4j
//public class ProcessPaymentUseCase {
//
////    private final PaymentRequestUseCase paymentRequestUseCase;
//    private final OrderGateway orderGateway;
//
//    public void exec(PaymentConsumerPayload dto)  {
//        log.info("Aqui passou");
//        UUID id = dto.orderId();
//        PaymentRequest paymentRequest = orderGateway.findById(id);
//        log.info("Encontrou a ordem");
////        PaymentResponse paymentResponse = paymentRequestUseCase.exec(paymentRequest);
////        log.info("Fez a requisição");
////        orderGateway.updateOrderStatus(id, OrderStatus.approved);
//        log.info("O pagamento foi processado!!!!");
//    }
//}
