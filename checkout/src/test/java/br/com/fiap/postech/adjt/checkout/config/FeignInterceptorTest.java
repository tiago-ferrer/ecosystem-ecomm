package br.com.fiap.postech.adjt.checkout.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import feign.RequestTemplate;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FeignInterceptorTest {

    @InjectMocks
    private FeignInterceptor feignInterceptor;

    @Mock
    private RequestTemplate requestTemplate;

    @Test
    void testApply() {
        feignInterceptor.apply(requestTemplate);
        verify(requestTemplate, times(1))
                .header(Mockito.anyString(), Mockito.anyString());
    }

}
