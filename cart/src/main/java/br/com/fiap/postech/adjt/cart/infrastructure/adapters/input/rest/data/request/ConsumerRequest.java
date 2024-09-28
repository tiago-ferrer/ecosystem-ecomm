package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerRequest {
    
    @org.hibernate.validator.constraints.UUID(message = "\"error\": \"Invalid consumerId format\"")
    private UUID consumerId;
   
}

