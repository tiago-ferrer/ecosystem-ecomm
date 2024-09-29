package br.com.fiap.postech.adjt.checkout.service.impl;


import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.payment.Field;
import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import br.com.fiap.postech.adjt.checkout.repository.FieldRepository;
import br.com.fiap.postech.adjt.checkout.repository.PaymentRepository;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import br.com.fiap.postech.adjt.checkout.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl  implements PaymentService {

    private final PaymentRepository repository;
    private final OrderService service;
    private final FieldRepository fieldRepository;

    public PaymentServiceImpl(PaymentRepository repository, OrderService service, FieldRepository fieldRepository) {
        this.repository = repository;
        this.service = service;
        this.fieldRepository = fieldRepository;
    }

    public Payment createPayment(Payment payment){
        Field field = fieldRepository.save(payment.getField());
        payment.setField(field);
        return repository.save(payment);

    }

    public PaymentResponseDto validarPayment(PaymentResponseDto responseDto) {
        if (responseDto.getPaymentId()== null ) {
            throw new InvalidOrderIdException("Invalid orderId format");
        } else {
            var existe = service.getById(UUID.fromString(responseDto.getPaymentId()));
            if (existe != null) {
                var status = PaymentStatus.valueOf(responseDto.getStatus());
                if (!status.equals(existe.getPaymentStatus())) {
                    existe.setPaymentStatus(status);
                    service.updateStatus(existe);
                }
            }
            return responseDto;
        }
    }

}
