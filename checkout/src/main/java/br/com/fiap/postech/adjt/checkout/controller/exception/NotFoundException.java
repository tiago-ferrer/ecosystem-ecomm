package br.com.fiap.postech.adjt.checkout.controller.exception;

public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
        super(message);
    }
}
