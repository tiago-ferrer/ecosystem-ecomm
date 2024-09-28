package br.com.fiap.postech.adjt.checkout.clients.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

class CartClientImplTest {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private CartClientImpl cartClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartClient = new CartClientImpl(restClient);
    }

    @Test
    void testConsultCart_Success() {
     
    }
}