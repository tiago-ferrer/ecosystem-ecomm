package br.com.fiap.postech.adjt.checkout.consumer;



import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="api", url="https://payment-api-latest.onrender.com")
public interface ApiPayment {
//    @PostMapping("/create-group")
//    ApiKeyGroup createApiGroup(@RequestBody ApiGroup apiGroup);

   @PostMapping("/create-payment")
   PaymentResponseDto createPayment(@RequestBody PaymentRequestDto payment,
                                    @RequestHeader("apiKey") String apiKey);
}
