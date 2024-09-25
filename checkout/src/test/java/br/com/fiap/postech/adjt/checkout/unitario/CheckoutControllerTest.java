package br.com.fiap.postech.adjt.checkout.unitario;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.CheckoutController;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.CamposMetodoPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.MetodoPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.PagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.SolicitaPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.useCase.checkout.CheckoutUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

public class CheckoutControllerTest {

    @Test
    public void processa_sucesso() {
        // preparação
        var service = Mockito.mock(CheckoutUseCase.class);
        Mockito.when(service.processa(
                            any(SolicitaPagamentoRequestDTO.class)
                        )
                )
                .thenReturn(
                        new PagamentoResponseDTO(
                                "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                "Pagamento realizado com sucesso"
                        )
                );

        var controller = new CheckoutController(service);

        // execução
        var processa = controller.processa(new SolicitaPagamentoRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                100L,
                "BRL",
                new MetodoPagamentoRequestDTO(
                        "br_credit_card",
                        new CamposMetodoPagamentoRequestDTO(
                                "4111111111111111",
                                "12",
                                "25",
                                "123",
                                "Fulano de Tal"
                        )
                )
        ));

        // avaliação
        Assertions.assertEquals(HttpStatus.OK, processa.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void processa_camposInvalidos(final String consumerId,
                                         final Long amount,
                                         final String currency,
                                         final String methodPaymentType,
                                         final String number,
                                         final String expirationMonth,
                                         final String expirationYear,
                                         final String cvv,
                                         final String name) {
        // preparação
        var service = Mockito.mock(CheckoutUseCase.class);
        Mockito.doThrow(
                        new IllegalArgumentException("Campos inválidos!")
                )
                .when(service)
                .processa(
                        any(SolicitaPagamentoRequestDTO.class)
                );

        var controller = new CheckoutController(service);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            var processa = controller.processa(new SolicitaPagamentoRequestDTO(
                    consumerId.equals("-1") ? null : consumerId,
                    amount == -1 ? null : amount,
                    currency.equals("-1") ? null : currency,
                    new MetodoPagamentoRequestDTO(
                            methodPaymentType.equals("-1") ? null : methodPaymentType,
                            new CamposMetodoPagamentoRequestDTO(
                                    number.equals("-1") ? null : number,
                                    expirationMonth.equals("-1") ? null : expirationMonth,
                                    expirationYear.equals("-1") ? null : expirationYear,
                                    cvv.equals("-1") ? null : cvv,
                                    name.equals("-1") ? null : name
                            )
                    )
            ));
        });
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of("-1", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7OI", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(" ", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("teste", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", -1L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 0L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", -2L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "-1", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, " ", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "-1", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", " ", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "-1", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", " ", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "", "12", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "-1", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", " ", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "a", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "abc", "25", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "-1", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", " ", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "a", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "abc", "123", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", "-1", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", " ", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", "", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", "a", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", "abcd", "Nome Teste"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "-1"),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", " "),
                Arguments.of("e7c5c208-c4c3-42fc-9370-3141309cb7d0", 10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "")
        );
    }

}
