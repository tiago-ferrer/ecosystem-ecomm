package br.com.fiap.postech.adjt.checkout.integracao;

import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
@Slf4j
public class PaymentServiceAPI {

    @Qualifier("webClientPayment")
    private final WebClient webClient;

    private final RestTemplate restTemplate;

    @Value("${API_KEY}")
    private String apiKey;

    @Value("${URL_PAYMENT}")
    private String urlPayment;


    public PaymentServiceAPI(WebClient webClient, RestTemplate restTemplate) {
        this.webClient = webClient;
        this.restTemplate = restTemplate;
    }

    public Mono<PaymentResponseDto> processPayment(PaymentRequestDto paymentRequestDto) {
        return webClient.post()
//                .uri("/create-payment")
                .body(BodyInserters.fromValue(paymentRequestDto))
                .header("apiKey",apiKey)
                .retrieve()
                .bodyToMono(PaymentResponseDto.class)
                .doOnSuccess(response -> log.info("Chamada à API externa realizada com sucesso " + response))
                .doOnError(error -> log.error("Erro ao chamar API externa", error));
    }



//    @Async
    public PaymentResponseDto sendPayment(PaymentRequestDto paymentRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", apiKey);
        HttpEntity<Object> httpEntity = new HttpEntity<>(paymentRequestDto, headers);
        try{
            log.info("chamando api de pagamentos - orderId: {}", paymentRequestDto.getOrderId());
            ResponseEntity<PaymentResponseDto> resp = restTemplate.exchange(
                    urlPayment, HttpMethod.POST, httpEntity, PaymentResponseDto.class);
            log.info("Resposta de api de pagamentos - orderId: {}", resp.getBody().getPaymentId());
            return Optional.of(resp).map(ResponseEntity::getBody).orElse(null);
        } catch(Exception e) {
            return null;
        }
    }


}
