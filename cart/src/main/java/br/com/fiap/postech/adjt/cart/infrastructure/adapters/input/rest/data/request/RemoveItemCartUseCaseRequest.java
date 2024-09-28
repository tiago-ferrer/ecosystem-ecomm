package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
public class RemoveItemCartUseCaseRequest {
    
	@Pattern(message = "Invalid consumerId format", regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String consumerId;

    @NotEmpty(message = "itemId may not be empty")
    private String itemId;
    
}

