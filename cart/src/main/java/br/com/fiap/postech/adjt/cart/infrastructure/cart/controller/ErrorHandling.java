package br.com.fiap.postech.adjt.cart.infrastructure.cart.controller;

import br.com.fiap.postech.adjt.cart.domain.exception.ApiProductsException;
import br.com.fiap.postech.adjt.cart.domain.exception.ErrorInternalException;
import br.com.fiap.postech.adjt.cart.domain.exception.ErrorTreatedException;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ErrorHandlingResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandling {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorHandlingResponseDTO trataParametroInvalido(IllegalArgumentException ex) {
		return new ErrorHandlingResponseDTO(
				"Invalid consumerId format"
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ApiProductsException.class)
	public ErrorHandlingResponseDTO trataErroApiProducts(ApiProductsException ex) {
		return new ErrorHandlingResponseDTO(
				ex.getMessage()
		);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ErrorInternalException.class)
	public ErrorHandlingResponseDTO trataErroNaoMapeado(ErrorInternalException ex) {
		return new ErrorHandlingResponseDTO(
				ex.getMessage()
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ErrorTreatedException.class)
	public ErrorHandlingResponseDTO trataErroMapeado(ErrorTreatedException ex) {
		return new ErrorHandlingResponseDTO(
				ex.getMessage()
		);
	}

}
