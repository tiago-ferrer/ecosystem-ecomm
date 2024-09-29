package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.config.HttpClientCustom;
import br.com.fiap.postech.adjt.checkout.consumer.CartClient;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.repository.CartRepository;
import br.com.fiap.postech.adjt.checkout.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartClient cartClient;

    @Mock
    private HttpClientCustom httpClientCustom;

    @Mock
    private ModelMapper mapper;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    AutoCloseable closeMocks;

    @BeforeEach
    void setup() {
        closeMocks = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(cartService, "urlCart", "http://localhost:8080");
    }

    @AfterEach
    void close() throws Exception {
        closeMocks.close();
    }


    @Test
    void testFindByConsumerIdSuccess() {
        when(httpClientCustom.handler(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any()))
                .thenReturn(ResponseEntity.ok(TestUtils.buildMapCartItems()));
        when(cartRepository.save(Mockito.any())).thenReturn(TestUtils.buildCart());
        CartDto cartDto = cartService.findByConsumerId(TestUtils.buildCartRequest());
        assertNotNull(cartDto);
        assertInstanceOf(CartDto.class, cartDto);
        assertEquals(1, cartDto.getItems().size());
    }

}
