package br.com.fiap.postech.adjt.checkout.dataprovider.integration.mappers;

import br.com.fiap.postech.adjt.checkout.dataprovider.integration.dto.CartByConsumerDTO;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CartMapper {
    @Mapping(target = "itemId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    CartModel.CartItensModel toCartItensModel(CartByConsumerDTO.Item item);
}
