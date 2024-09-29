package br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto;

import java.util.List;

public record InfoItensResponseDTO(

		List<ItensDetailsResponseDTO> items
) {}
