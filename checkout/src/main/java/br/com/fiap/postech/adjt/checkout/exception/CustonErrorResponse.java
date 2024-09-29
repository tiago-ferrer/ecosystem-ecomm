package br.com.fiap.postech.adjt.checkout.exception;

import lombok.Data;

@Data
public class CustonErrorResponse {
    private String error;

    public CustonErrorResponse(String error) {
        this.error = error;
    }
}
