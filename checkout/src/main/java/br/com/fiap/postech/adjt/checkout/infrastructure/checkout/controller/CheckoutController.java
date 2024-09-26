package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.BuscaListaPagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.BuscaPagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.PagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.SolicitaPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.useCase.checkout.CheckoutUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	public static final String URL_CONSUMER_ID = "/{consumerId}";
	public static final String URL_ORDER_ID = "/by-order-id/{orderId}";

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

	@Operation(
			summary = "Busca todos os pagamentos de um usuario."
	)
	@GetMapping(URL_CONSUMER_ID)
	public ResponseEntity<BuscaListaPagamentoResponseDTO> busca(@PathVariable final String consumerId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.busca(consumerId));

	}

	@Operation(
			summary = "Busca um pagamento em especifico."
	)
	@GetMapping(URL_ORDER_ID)
	public ResponseEntity<BuscaPagamentoResponseDTO> buscaPorOrderId(@PathVariable final String orderId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(this.service.buscaPorOrderId(orderId));

	}

}
