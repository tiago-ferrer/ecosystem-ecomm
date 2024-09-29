package br.com.fiap.postech.adjt.cart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartRequest {


    @NotBlank(groups = {CartView.AddItem.class, CartView.GetDelete.class, CartView.Update.class},
            message = "Invalid consumerId format"
    )
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid consumerId format",
            groups = {CartView.AddItem.class, CartView.GetDelete.class, CartView.Update.class}
    )
    @JsonView({CartView.AddItem.class, CartView.GetDelete.class, CartView.Update.class})
    private String consumerId;


    @Pattern(
            regexp = "^[0-9]+$",
            message = "Invalid itemId does not exist",
            groups = {CartView.AddItem.class, CartView.Update.class}
    )
    @NotBlank(groups = {CartView.AddItem.class, CartView.Update.class}, message = "Invalid itemId does not exist")
    @JsonView({CartView.AddItem.class, CartView.Update.class})
    private String itemId;


    @Min(
            value = 1,
            message = "Invalid itemId quantity",
            groups = {CartView.AddItem.class}
    )
    @NotNull(groups = {CartView.AddItem.class}, message = "Invalid itemId quantity")
    @JsonView({CartView.AddItem.class})
    private Integer quantity;


    public interface CartView {
        public static interface AddItem {}
        public static interface GetDelete {}
        public static interface Update {}
    }

    public UUID consumerIdToUUID() {
        return UUID.fromString(this.getConsumerId());
    }

}
