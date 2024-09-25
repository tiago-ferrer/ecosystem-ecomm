package br.com.fiap.postech.adjt.gateway.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadRequestException extends RuntimeException {

    private HttpStatusCode statusCode;
    private String message;

    public BadRequestException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

}
