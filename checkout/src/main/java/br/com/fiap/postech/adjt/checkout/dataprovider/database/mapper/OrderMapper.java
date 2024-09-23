package br.com.fiap.postech.adjt.checkout.dataprovider.database.mapper;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {
        OrderEntity.class,
        OrderModel.class
})
public interface OrderMapper {
    OrderEntity toOrderEntity(final OrderModel orderModel);
    OrderModel toOrderModel(OrderEntity orderEntity);
}
