package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
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
public class ItemCartRequest {
    
    @org.hibernate.validator.constraints.UUID(message = "\"error\": \"Invalid consumerId format\"")
    private UUID consumerId;

    @NotEmpty(message = "itemId may not be empty")
    private String itemId;
    
    @Positive(message = "Quantity items may not be less than 1")
    private Integer quantity;
    
}

