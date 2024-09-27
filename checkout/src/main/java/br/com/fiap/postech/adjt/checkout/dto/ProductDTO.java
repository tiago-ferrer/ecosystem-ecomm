package br.com.fiap.postech.adjt.checkout.dto;
import java.util.UUID;

public record ProductDTO(UUID id, String name, double price) {}
