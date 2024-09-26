package br.com.fiap.postech.adjt.checkout.mapper;

import br.com.fiap.postech.adjt.checkout.dto.PaymentMethodRequestDTO;
import br.com.fiap.postech.adjt.checkout.model.PaymentMethod;

public interface PaymentMethodMapper {

    static PaymentMethod toEntity(PaymentMethodRequestDTO dto) {
        return new PaymentMethod(
                dto.type(),
                PaymentMethodFieldsMapper.toEntity(dto.fields())
        );
    }
}