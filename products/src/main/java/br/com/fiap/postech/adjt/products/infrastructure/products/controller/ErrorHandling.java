package br.com.fiap.postech.adjt.products.infrastructure.products.controller;

import br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto.ErrorHandlingResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandling {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorHandlingResponseDTO trataParametroInvalido(IllegalArgumentException ex) {
		if(ex.getMessage().equals("Item não encontrado")) {
			return new ErrorHandlingResponseDTO(
					1,
					"Item nao encontrado"
			);

		}
		else if(ex.getMessage().equals("quantity não é um numero valido")) {
			return new ErrorHandlingResponseDTO(
					2,
					"Quantidade invalida"
			);

		}
		return new ErrorHandlingResponseDTO(
				0,
				"ERRO NAO MAPEADO, ENTRAR EM CONTATO COM O SUPORTE"
		);
	}

}
