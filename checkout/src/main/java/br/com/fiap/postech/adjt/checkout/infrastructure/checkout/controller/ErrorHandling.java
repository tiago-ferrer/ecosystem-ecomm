package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller;

import br.com.fiap.postech.adjt.checkout.domain.exception.ApiCartException;
import br.com.fiap.postech.adjt.checkout.domain.exception.ErrorTreatedException;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.ErrorHandlingResponseDTO;
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
				"Invalid payment information"
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ApiCartException.class)
	public ErrorHandlingResponseDTO trataErroApiCart(ApiCartException ex) {
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
