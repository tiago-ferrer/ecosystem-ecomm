package br.com.fiap.postech.adjt.products.unitario;

import br.com.fiap.postech.adjt.products.infrastructure.products.controller.ProductsController;
import br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto.ItemResponseDTO;
import br.com.fiap.postech.adjt.products.useCase.products.impl.ProductsUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

public class ProductsControllerTest {

    @Test
    public void busca_sucesso() {
        // preparação
        var service = Mockito.mock(ProductsUseCaseImpl.class);
        Mockito.when(service.busca(
                                any(Long.class),
                                any(Long.class)
                        )
                )
                .thenReturn(
                        new ItemResponseDTO(
                                1L,
                                "Item Teste",
                                new BigDecimal("100")
                        )
                );

        var controller = new ProductsController(service);

        // execução
        var item = controller.busca(1L, 1L);

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, item.getStatusCode());
    }

    @Test
    public void busca_itemNaoExisteOuIndisponibilidadeNaApiExternaOuCamposInvalidos() {
        // preparação
        var service = Mockito.mock(ProductsUseCaseImpl.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Qualquer erro")
                )
                .when(service)
                .busca(
                        any(Long.class),
                        any(Long.class)
                );

        var controller = new ProductsController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            controller.busca(1L, 1L);
        });

        // avaliação
        Assertions.assertEquals("Qualquer erro", excecao.getMessage());
    }

}
