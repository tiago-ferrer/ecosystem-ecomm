package br.com.fiap.postech.adjt.checkout.domain.model.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartModel {
    List<CartItensModel> items;
}
