package br.com.fiap.postech.adjt.checkout.dataprovider.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartByConsumerDTO {
    public String consumerId;
    public List<Item> items;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Item {
        public String productId;
        public int quantity;
    }
}
