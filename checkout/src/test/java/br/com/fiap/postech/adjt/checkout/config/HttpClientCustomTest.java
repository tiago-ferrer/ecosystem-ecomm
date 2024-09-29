package br.com.fiap.postech.adjt.checkout.config;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HttpClientCustomTest {

    @InjectMocks
    private HttpClientCustom httpClientCustom;

    @Test
    void testHandler() {
        ResponseEntity<Object> resp = httpClientCustom
                .handler("http://algumaurl:8080", HttpMethod.GET, TestUtils.buildCartDto());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
    }


}
