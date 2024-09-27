package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.adjt.cart.application.ports.input.GetItemsCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.SaveItemCartUseCase;
import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.ItemCartRequest;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.response.ProductResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class CartRestAdapter {
    
    private final SaveItemCartUseCase createItemCartUseCase;

    private final GetItemsCartUseCase getItemsCartUseCase;

    private final ModelMapper mapper;

    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ItemCartRequest itemCartRequest) {
        // Request to domain
        ItemCart itemCart = mapper.map(itemCartRequest, ItemCart.class);
        
        itemCart = createItemCartUseCase.createItemCart(itemCart);
        // Domain to response
        return new ResponseEntity<>(mapper.map(itemCart, ProductResponse.class), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<ProductResponse> getProduct(@RequestBody UUID itemId){
        ItemCart product = getItemsCartUseCase.getItemCartByItemId(itemId);
        // Domain to response
        return new ResponseEntity<>(mapper.map(product, ProductResponse.class), HttpStatus.OK);
    }
    
}
