package br.com.fiap.postech.adjt.checkout.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class AppConfigTest {

    @InjectMocks
    private AppConfig appConfig;

    @Test
    void testRestTemplate() {
        assertInstanceOf(RestTemplate.class, appConfig.restTemplate());
    }

    @Test
    void testModelMapper() {
        assertInstanceOf(ModelMapper.class, appConfig.modelMapper());
    }

}
