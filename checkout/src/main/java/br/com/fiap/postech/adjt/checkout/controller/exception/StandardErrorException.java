package br.com.fiap.postech.adjt.checkout.controller.exception;

public class StandardErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final StandardError standardError;

    public StandardErrorException(StandardError standardError) {
        this.standardError = standardError;
    }

    public StandardError getStandardError() {
        return standardError;
    }
}
