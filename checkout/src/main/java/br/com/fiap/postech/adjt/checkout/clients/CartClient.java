package br.com.fiap.postech.adjt.checkout.clients;

import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.MessageResponse;

public interface CartClient {
    CartResponse consult(FindCartByCustomerIdRequest request);

    MessageResponse clear(ClearCartRequest request);
}
