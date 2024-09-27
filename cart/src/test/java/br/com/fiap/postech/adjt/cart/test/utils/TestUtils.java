package br.com.fiap.postech.adjt.cart.test.utils;

import br.com.fiap.postech.adjt.cart.dto.*;
import br.com.fiap.postech.adjt.cart.model.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TestUtils {

    public static CartRequest buildCartRequest() {
        CartRequest cart = new CartRequest();
        cart.setConsumerId(createUuid().toString());
        cart.setItemId("1");
        cart.setQuantity(10);
        return cart;
    }

    public static ProductDto buildProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle("Fjallraven");
        productDto.setPrice(BigDecimal.valueOf(109.95));
        productDto.setDescription("Your perfect pack");
        productDto.setCategory("men's clothing");
        productDto.setImage("https://fakestoreapi.com/img/img_product_1.jpg");
        productDto.setRating(new RatingDto(BigDecimal.valueOf(3.9), 120L));
        return productDto;
    }

    public static Item buildItem() {
        Item.ItemColumnsId id = new Item.ItemColumnsId();
        id.setConsumerId(createUuid());
        id.setItemId(1L);
        Item item = new Item();
        item.setQnt(10);
        item.setId(id);
        return item;
    }

    public static CartDto buildCartDto() {
        ItemDto item = new ItemDto(1L,4);
        return new CartDto(List.of(item));
    }

    public static UUID createUuid() {
        return UUID.fromString("7c89018b-b421-41e1-aa3d-0c9db747f614");
    }

}


