package br.com.fiap.postech.adjt.checkout.application.mappers;

import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentCheckoutDTO;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import org.mapstruct.Mapper;

@Mapper
public interface CheckoutMapper {
    CheckoutModel toCheckoutModel(final CheckoutDTO checkoutDTO);
    PaymentCheckoutDTO toPaymentCheckoutDTO(final CheckoutModel checkoutModel);
}
