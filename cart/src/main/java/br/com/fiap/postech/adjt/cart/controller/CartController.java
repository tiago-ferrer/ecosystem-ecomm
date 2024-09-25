package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.controller.request.AddItemRequest;
import br.com.fiap.postech.adjt.cart.controller.request.ChangeItemRequest;
import br.com.fiap.postech.adjt.cart.controller.request.FindCartRequest;
import br.com.fiap.postech.adjt.cart.controller.request.RemoveAllItemsRequest;
import br.com.fiap.postech.adjt.cart.controller.response.CartResponse;
import br.com.fiap.postech.adjt.cart.controller.response.ItemResponse;
import br.com.fiap.postech.adjt.cart.controller.response.MessageResponse;
import br.com.fiap.postech.adjt.cart.entity.Cart;
import br.com.fiap.postech.adjt.cart.exception.EmptyCartException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemQuantityException;
import br.com.fiap.postech.adjt.cart.exception.InvalidUUIDException;
import br.com.fiap.postech.adjt.cart.service.CartService;
import br.com.fiap.postech.adjt.cart.validator.UUIDValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(@RequestBody AddItemRequest addItemRequest) {
        try {
            if (UUIDValidator.isValidUUID(addItemRequest.consumerId())) {
                UUID consumerId = UUID.fromString(addItemRequest.consumerId());
                Long itemId = Long.parseLong(addItemRequest.itemId());
                Long quantity = Long.parseLong(addItemRequest.quantity());
                cartService.addItem(consumerId, itemId, quantity);
                return ResponseEntity.ok().body(new MessageResponse("Item added to cart successfully"));
            }
            throw new InvalidUUIDException();
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid consumerId format");
        } catch (InvalidItemException | InvalidItemQuantityException | InvalidUUIDException e)  {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> removeItem(@RequestBody ChangeItemRequest changeItemRequest) {
        try {
            if (UUIDValidator.isValidUUID(changeItemRequest.consumerId())) {
                UUID consumerId = UUID.fromString(changeItemRequest.consumerId());
                cartService.removeItem(consumerId, changeItemRequest.itemId());
                return ResponseEntity.ok().body(new MessageResponse("Item removed from cart successfully"));
            }
            throw new InvalidUUIDException();
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid consumerId format");
        } catch (InvalidItemException | EmptyCartException | InvalidUUIDException e)  {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/item")
    public ResponseEntity<?> incrementItem(@RequestBody ChangeItemRequest changeItemRequest) {
        try {
            if (UUIDValidator.isValidUUID(changeItemRequest.consumerId())) {
                UUID consumerId = UUID.fromString(changeItemRequest.consumerId());
                cartService.incrementItem(consumerId, changeItemRequest.itemId());
                return ResponseEntity.ok().body(new MessageResponse("Item incremented from cart successfully"));
            }
            throw new InvalidUUIDException();
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid consumerId format");
        } catch (InvalidItemException | InvalidUUIDException e)  {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findCart(@RequestBody FindCartRequest findCartRequest) {
        try {
            if (UUIDValidator.isValidUUID(findCartRequest.consumerId())) {
                UUID consumerId = UUID.fromString(findCartRequest.consumerId());
                Cart cart = cartService.findCart(consumerId);
                List<ItemResponse> itemsResponse = cart.getItems().stream().map(item -> new ItemResponse(item.getItemId(), item.getQnt())).toList();
                return ResponseEntity.ok(new CartResponse(itemsResponse));
            }
            throw new InvalidUUIDException();
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid consumerId format");
        } catch (EmptyCartException | InvalidUUIDException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeAllItems(@RequestBody RemoveAllItemsRequest removeAllItemsRequest) {
        try {
            if (UUIDValidator.isValidUUID(removeAllItemsRequest.consumerId())) {
                UUID consumerId = UUID.fromString(removeAllItemsRequest.consumerId());
                cartService.removeAllItems(consumerId);
                return ResponseEntity.ok().body(new MessageResponse("Items removed from cart successfully"));
            }
            throw new InvalidUUIDException();
        } catch(IllegalArgumentException | InvalidUUIDException e) {
            return ResponseEntity.badRequest().body("Invalid consumerId format");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
