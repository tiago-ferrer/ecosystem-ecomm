package br.com.fiap.postech.adjt.cart.domain.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestApiErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CartNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleMethodArgumentNotValid(CartNotFoundException ex) {
		return new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}

}
