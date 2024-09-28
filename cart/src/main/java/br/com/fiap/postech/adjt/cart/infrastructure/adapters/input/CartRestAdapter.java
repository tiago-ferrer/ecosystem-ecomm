package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input;

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
import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.ConsumerRequest;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.ItemCartRequest;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.ItemCartResponseMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.response.CartResponse;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.response.ItemCartResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class CartRestAdapter {
    
    private final SaveItemCartUseCase createItemCartUseCase;

    private final GetItemsCartUseCase getItemsCartUseCase;

    private final ModelMapper mapper;

    @PostMapping()
    public ResponseEntity<ItemCartResponse> createProduct(@RequestBody ItemCartRequest itemCartRequest) {
        // Request to domain
    	
    	Cart cart = ItemCartResponseMapper.map(itemCartRequest);
    	
        createItemCartUseCase.createItemCart(cart);
        // Domain to response
        return new ResponseEntity<>(new ItemCartResponse("Item added to cart successfully"), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<CartResponse> getItems(@RequestBody ConsumerRequest customerRequest) {
        Cart cart = getItemsCartUseCase.getItemCartByItemId(customerRequest.getConsumerId());
        // Domain to response
        return new ResponseEntity<>(mapper.map(cart, CartResponse.class), HttpStatus.OK);
    }
    
}
