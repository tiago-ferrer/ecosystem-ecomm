package br.com.fiap.postech.adjt.cart.controller.impl;

import br.com.fiap.postech.adjt.cart.controller.CartController;
import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.IncrementCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.RemoveCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.response.FindCartByCustomerIdResponse;
import br.com.fiap.postech.adjt.cart.model.dto.response.MessageResponse;
import br.com.fiap.postech.adjt.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Aplicação de Gerenciamento de Carrinho de Compras.")
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    public CartControllerImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Adição de item no Carrinho",
            description = "Adiciona um item ao carrinho de um consumidor específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item incluído no carrinho com sucesso."
                            ),
                    @ApiResponse(responseCode = "400", description = "Inconsistência nos dados recebidos."),
            }
    )
    @Override
    @PostMapping("/items")
    public ResponseEntity<MessageResponse> add(@RequestBody AddCartItemRequest request) {
        cartService.add(request);

        return ResponseEntity.ok(new MessageResponse("Item added to cart successfully"));
    }

    @Operation(
            summary = "Remoção de item no Carrinho",
            description = "Remove uma unidade do item específico do carrinho de um consumidor. Caso o item atinga " +
                    "quantidade zero, o item é removido do carrinho.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item removido do carrinho com sucesso."
                    ),
                    @ApiResponse(responseCode = "400", description = "Inconsistência nos dados recebidos."),
            }
    )
    @Override
    @DeleteMapping("/item")
    public ResponseEntity<MessageResponse> remove(@RequestBody RemoveCartItemRequest request) {
        cartService.remove(request);

        return ResponseEntity.ok(new MessageResponse("Item removed from cart successfully"));
    }

    @Operation(
            summary = "Incremento de item no Carrinho",
            description = "Incrementa uma unidade de um item específico do carrinho de um consumidor.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item incrementado no carrinho com sucesso."
                    ),
                    @ApiResponse(responseCode = "400", description = "Inconsistência nos dados recebidos."),
            }
    )
    @Override
    @PutMapping("/item")
    public ResponseEntity<MessageResponse> increment(@RequestBody IncrementCartItemRequest request) {
        cartService.increment(request);

        return ResponseEntity.ok(new MessageResponse("Item removed from cart successfully"));
    }

    @Operation(
            summary = "Busca carrinho do consumidor",
            description = "Retorna o carrinho de um consumidor específico.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Carrinho encontrado e retornado com sucesso."
                    ),
                    @ApiResponse(responseCode = "400", description = "Inconsistência nos dados recebidos."),
            }
    )
    @Override
    @GetMapping
    public ResponseEntity<FindCartByCustomerIdResponse> findByCustomerId(@RequestBody FindCartByCustomerIdRequest request) {
        FindCartByCustomerIdResponse items = cartService.findByCustomerId(request.consumerId());

        return ResponseEntity.ok(items);
    }

    @Operation(
            summary = "Limpar o Carrinho",
            description = "Remove todo o carrinho de um consumidor.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Itens removidos do carrinho com sucesso."
                    ),
                    @ApiResponse(responseCode = "400", description = "Inconsistência nos dados recebidos."),
            }
    )
    @Override
    @DeleteMapping
    public ResponseEntity<MessageResponse> clear(@RequestBody ClearCartRequest request) {
        cartService.clear(request.consumerId());

        return ResponseEntity.ok(new MessageResponse("Item removed from cart successfully"));
    }
}
