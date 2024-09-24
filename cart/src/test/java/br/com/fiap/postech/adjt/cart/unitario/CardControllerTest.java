package br.com.fiap.postech.adjt.cart.unitario;

import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.CartController;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ItemResponseDTO;
import br.com.fiap.postech.adjt.cart.useCase.cart.impl.CartUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;

public class CardControllerTest {

    @Test
    public void adiciona_sucesso() {
        // preparação
        var service = Mockito.mock(CartUseCaseImpl.class);
        Mockito.when(service.adiciona(
                            any(AdicionaItemRequestDTO.class)
                        )
                )
                .thenReturn(
                        new ItemResponseDTO(
                                "Item added to cart successfully"
                        )
                );

        var controller = new CartController(service);

        // execução
        var item = controller.adiciona(new AdicionaItemRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                123456L,
                1L
        ));

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, item.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-1",
            "teste",
            " ",
            "",
            "e7c5c208-c4c3-42fc-9370-3141309cb7OI"
    })
    public void adiciona_consumerIdInvalido(final String consumerId) {
        // preparação
        var service = Mockito.mock(CartUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(service)
                .adiciona(
                        any(AdicionaItemRequestDTO.class)
                );

        var controller = new CartController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            controller.adiciona(
                    new AdicionaItemRequestDTO(
                            consumerId.equals("-1") ? null : consumerId,
                            123456L,
                            1L
                    )
            );
        });
    }

}
