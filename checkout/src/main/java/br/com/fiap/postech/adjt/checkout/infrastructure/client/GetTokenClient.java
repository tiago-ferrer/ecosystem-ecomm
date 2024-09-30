package br.com.fiap.postech.adjt.checkout.infrastructure.client;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetTokenPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "token", url = "https://payment-api-latest.onrender.com")
public interface GetTokenClient {
    @PostMapping("/create-group")
    GetTokenResponse exec(GetTokenPayload dto);
}
