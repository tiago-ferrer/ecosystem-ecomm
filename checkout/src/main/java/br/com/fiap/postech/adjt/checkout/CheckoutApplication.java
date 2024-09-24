package br.com.fiap.postech.adjt.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients("br.com.fiap.postech")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class CheckoutApplication {
	public static void main(String[] args) {
		SpringApplication.run(CheckoutApplication.class, args);
	}
}