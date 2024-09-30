package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.adjt.cart.application.ports.input.ClearCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.GetItemsCartByCustomerIdUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.RemoveItemCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.SaveItemCartUseCase;
import br.com.fiap.postech.adjt.cart.application.ports.input.UpdateItemCartUseCase;
import br.com.fiap.postech.adjt.cart.domain.model.Cart;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.ConsumerItemsCartRequest;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.ItemCartRequest;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.ItemCartResponseMapper;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.RemoveItemCartUseCaseRequest;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.request.UpdateItemCartUseCaseRequest;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.response.CartResponse;
import br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.response.ItemCartResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class CartRestAdapter {

    private final SaveItemCartUseCase createItemCartUseCase;

    private final RemoveItemCartUseCase removeItemCartUseCase;

    private final UpdateItemCartUseCase updateItemCartUseCase;

    private final GetItemsCartByCustomerIdUseCase getItemsCartByCustomerIdUseCase;

    private final ClearCartUseCase clearCartUseCase;

    private final ModelMapper mapper;

    @PostMapping("/items")
    public ResponseEntity<ItemCartResponse> addItemCart(@RequestBody @Validated ItemCartRequest itemCartRequest) {
        // Request to domain

        Cart cart = ItemCartResponseMapper.map(itemCartRequest);

        createItemCartUseCase.createItemCart(cart);
        // Domain to response
        return new ResponseEntity<>(new ItemCartResponse("Item added to cart successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/item")
    public ResponseEntity<ItemCartResponse> removeItemsCart(
            @RequestBody @Validated RemoveItemCartUseCaseRequest removeItemCartUseCaseRequest) {

        Cart cart = ItemCartResponseMapper.mapRemoveItemCartUseCaseRequest(removeItemCartUseCaseRequest);

        removeItemCartUseCase.removeItemCart(cart);

        return new ResponseEntity<>(new ItemCartResponse("Item removed from cart successfully"), HttpStatus.OK);
    }

    @PutMapping("/item")
    public ResponseEntity<ItemCartResponse> updateItemsCart(
            @RequestBody @Validated UpdateItemCartUseCaseRequest updateItemCartUseCaseRequest) {

        Cart cart = ItemCartResponseMapper.mapUpdateItemCartUseCaseRequest(updateItemCartUseCaseRequest);

        updateItemCartUseCase.updateItemCartUseCase(cart);

        // msg errada na especificacao
        return new ResponseEntity<>(new ItemCartResponse("Item removed from cart successfully"), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<CartResponse> getItemsCartByCustomerId(
            @RequestBody @Validated ConsumerItemsCartRequest consumerItemsCartRequest) {
        Cart cart = getItemsCartByCustomerIdUseCase.getItemsCartByCustomerId(consumerItemsCartRequest.getConsumerId());
        return new ResponseEntity<>(mapper.map(cart, CartResponse.class), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<ItemCartResponse> clearCartByCustomerId(
            @RequestBody @Validated ConsumerItemsCartRequest consumerItemsCartRequest) {

        clearCartUseCase.clearCartByCustomerId(consumerItemsCartRequest.getConsumerId());

        return new ResponseEntity<>(new ItemCartResponse("Items removed from cart successfully"), HttpStatus.OK);
    }

}
