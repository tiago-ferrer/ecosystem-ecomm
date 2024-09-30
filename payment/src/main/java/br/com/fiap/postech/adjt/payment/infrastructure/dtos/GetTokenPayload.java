package br.com.fiap.postech.adjt.payment.infrastructure.dtos;

import java.util.List;

public record GetTokenPayload(String groupName, List<String> studentNames) {
}
