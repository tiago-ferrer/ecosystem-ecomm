package br.com.fiap.postech.adjt.checkout.application.exception.handle;

import br.com.fiap.postech.adjt.checkout.application.dto.ErrorDTO;
import br.com.fiap.postech.adjt.checkout.application.exception.CustomException;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest(ControllerExceptionHandler.class)
public class ControllerExceptionHandlerTest {

    @Autowired
    private ControllerExceptionHandler handler;

    @MockBean
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Test
    public void handleCustomExceptions_WhenCalled_ShouldReturnResponseEntity() {
        CustomException customException = new CustomException("Custom exception");
        ResponseEntity<ErrorDTO> responseEntity = handler.handleCustomExceptions(customException);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void handleGlobalException_WhenCalled_ShouldReturnResponseEntity() {
        Exception ex = new Exception("Global exception");
        ResponseEntity<ErrorDTO> responseEntity = handler.handleGlobalException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void handleValidationExceptions_WhenCalledWithAppException_ShouldReturnResponseEntity() {
        AppException appException = new AppException("App exception");
        ResponseEntity<ErrorDTO> responseEntity = handler.handleValidationExceptions(appException);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void handleValidationExceptions_WhenCalledWithMethodArgumentNotValidException_ShouldReturnResponseEntity() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        ResponseEntity<ErrorDTO> responseEntity = handler.handleValidationExceptions(methodArgumentNotValidException);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void handleGlobalException_WhenCalledWithUnhandledException_ShouldReturnInternalServerError() {
        Exception unhandledException = new IllegalStateException("Unhandled exception");
        ResponseEntity<ErrorDTO> responseEntity = handler.handleGlobalException(unhandledException);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

}