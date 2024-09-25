package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.PagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.SolicitaPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.useCase.checkout.CheckoutUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(
		name = "Checkout",
		description = """
				Esta aplicação é responsável por gerenciar o processo de checkout no ecossistema de e-commerce.
				Ele atua como o ponto central para a finalização de compras, garantindo que todas as etapas necessárias
				sejam executadas corretamente.
				"""
)
@RestController
public class CheckoutController {

	private final CheckoutUseCase service;

	public CheckoutController(final CheckoutUseCase service) {
		this.service = service;
	}

	@Operation(
			summary = "Processa o pagamento e cria um pedido."
	)
	@PostMapping
	public ResponseEntity<PagamentoResponseDTO> processa(@RequestBody @Valid final SolicitaPagamentoRequestDTO dadosPagamento) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.processa(dadosPagamento));

	}

}
