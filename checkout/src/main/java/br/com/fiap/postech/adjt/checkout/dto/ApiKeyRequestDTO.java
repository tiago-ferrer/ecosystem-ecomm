package br.com.fiap.postech.adjt.checkout.dto;

import java.util.List;

public record ApiKeyRequestDTO(String groupName,List<String> studentNames) {
}
