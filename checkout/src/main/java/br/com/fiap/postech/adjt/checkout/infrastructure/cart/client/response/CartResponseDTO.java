package br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response;

import java.util.List;

public record CartResponseDTO(

		List<CartDetailsResponseDTO> items
) {}
