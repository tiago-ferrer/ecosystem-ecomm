package br.com.fiap.postech.adjt.checkout.infrastructure.dtos;

import java.util.List;

public record GetTokenPayload(String groupName, List<String> studentNames) {
}
