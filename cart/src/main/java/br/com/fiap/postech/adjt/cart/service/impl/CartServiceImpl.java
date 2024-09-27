package br.com.fiap.postech.adjt.cart.service.impl;

import br.com.fiap.postech.adjt.cart.dto.*;
import br.com.fiap.postech.adjt.cart.exceptions.CartException;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.repository.ItemRepository;
import br.com.fiap.postech.adjt.cart.service.CartService;
import br.com.fiap.postech.adjt.cart.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final ItemRepository itemRepository;
    private final ProductService productService;

    @Override
    public ResponseEntity<CartResponseRecord> addItem(CartRequest cartRequest) {
        Item itemSave = new Item(cartRequest);
        productService.findProductById(Long.parseLong(cartRequest.getItemId()));
        itemRepository.findById(itemSave.getId())
                .ifPresentOrElse(item -> {
                    log.info("Item id {} existente, nao sera atualizado", item.getId());
                },() -> {
                    log.info("Adicionando item id {}", cartRequest.getItemId());
                    itemRepository.save(itemSave);
                });
        return ResponseEntity.ok(new CartResponseRecord(Messages.ADD_ITEM_SUCCESS));
    }

    @Override
    public ResponseEntity<CartResponseRecord> decrementItem(CartRequest cartRequest) {
        Optional<Item> itemLocate = Optional.of(new Item(cartRequest));
        itemLocate = itemRepository.findById(itemLocate.get().getId());
        AtomicBoolean decrement = new AtomicBoolean(false);
        itemLocate.filter(item -> item.getQnt() > 1)
                .ifPresent(item -> {
                    item.setQnt(item.getQnt() - 1);
                    itemRepository.save(item);
                    decrement.set(true);
                });
        itemLocate.filter(item -> item.getQnt() <= 1 && !decrement.get())
                .ifPresent(item -> {
                    itemRepository.deleteById(item.getId());
                });
        return ResponseEntity.ok(new CartResponseRecord(Messages.DEC_ITEM_SUCCESS));
    }

    @Override
    public ResponseEntity<CartResponseRecord> incrementItem(CartRequest cartRequest) {
        Item itemSave = new Item(cartRequest);
        itemSave = itemRepository.findById(itemSave.getId())
                .map(item -> {
                    log.info("Incrementando quantidade em item id {}", item.getId().getItemId());
                    item.setQnt(item.getQnt() + 1);
                    itemRepository.save(item);
                    return item;
                })
                .orElseThrow(() -> new CartException("Invalid itemId"));
        log.info("Item id {} - qtd: {}", itemSave.getId().getItemId(), itemSave.getQnt());
        return ResponseEntity.ok(new CartResponseRecord(Messages.INC_ITEM_SUCCESS));
    }

    @Override
    public ResponseEntity<CartDto> findCartByConsumerId(CartRequest cartRequest) {
        List<Item> itemsLocate = itemRepository.findAllByConsumerId(cartRequest.consumerIdToUUID());
        return Optional.ofNullable(itemsLocate)
                .filter(i -> !i.isEmpty())
                .map(i -> i.stream().map(ItemDto::new).toList())
                .map(CartDto::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CartException(Messages.CART_EMPTY));
    }

    @Override
    public ResponseEntity<CartResponseRecord> deleteCartByConsumerId(CartRequest cartRequest) {
        itemRepository.deleteByConsumerId(cartRequest.consumerIdToUUID());
        return ResponseEntity.ok(new CartResponseRecord(Messages.CART_CLEAR_ITEMS));
    }

}
