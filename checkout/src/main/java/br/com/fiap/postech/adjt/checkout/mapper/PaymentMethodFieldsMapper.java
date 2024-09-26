package br.com.fiap.postech.adjt.checkout.mapper;

import br.com.fiap.postech.adjt.checkout.dto.PaymentMethodFieldsRequestDTO;
import br.com.fiap.postech.adjt.checkout.model.PaymentMethodFields;

public interface PaymentMethodFieldsMapper {

    static PaymentMethodFields toEntity(PaymentMethodFieldsRequestDTO dto) {
        return new PaymentMethodFields(
                dto.number(),
                dto.expiration_month(),
                dto.expiration_year(),
                dto.cvv(),
                dto.name()
        );
    }
}