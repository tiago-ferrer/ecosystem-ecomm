package br.com.fiap.postech.adjt.checkout.dto.cart;


import br.com.fiap.postech.adjt.checkout.entity.cart.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto{
    private List<ItemDto> items;

    public CartDto convertToCartDto(Cart cart) {
        List<ItemDto> itemDtoList = cart.getItems().stream()
                .map(item -> new ItemDto(item.getItemId(), item.getQnt()))
                .collect(Collectors.toList());

        return new CartDto(itemDtoList);
    }

    public CartDto convertResponseEntityToCartDto(Map<String, Object> responseBody) {
        CartDto cartDto = new CartDto();
        List<ItemDto> itemDtoList = new ArrayList<>();

        Optional.ofNullable(responseBody)
                .map(resp -> resp.get("items"))
                .ifPresent(itemsCart -> {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) itemsCart;
                    for (Map<String, Object> item : items) {
                        ItemDto itemDto = new ItemDto();
                        itemDto.setItemId(((Number) item.get("itemId")).longValue());
                        itemDto.setQnt((Integer) item.get("qnt"));
                        itemDtoList.add(itemDto);
                    }
                });

        cartDto.setItems(itemDtoList);

        return cartDto;
    }

}
