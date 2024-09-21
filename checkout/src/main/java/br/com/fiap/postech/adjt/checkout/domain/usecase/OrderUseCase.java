package br.com.fiap.postech.adjt.checkout.domain.usecase;

import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.domain.entity.Order;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.exception.constants.ErrorConstants;
import br.com.fiap.postech.adjt.checkout.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderUseCase {

    private final OrderRepository orderRepository;

    public OrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CheckoutDTO checkoutDTO) throws AppException {

        /*TODO criar um serviço de integração com o carrinho para buscar o carrinho por customerID
        retornar com um cartDTO contendo os itens e seus valores ou caso dê erro retornar um AppException com a msg de carrinho sem itens
        */
        /*
         * Criar um objeto order e salvar ele
         * */
        /*
         * Criar um serviço de pagamento para chamar o pagamento e ficar aguardando o retorno dele
         */

        /*
         * Retornar um CheckoutResponseDTO ou lançar um AppException
         * */

        return null;
    }

    public List<Order> getOrderByConsumerId(String consumerId) throws AppException {

        if (Objects.isNull(consumerId) || consumerId.isEmpty() || isValidUUID(consumerId)) {
            throw new AppException(ErrorConstants.USER_ID_FORMAT_INVALID);
        }

        return orderRepository.findByCustomerId(consumerId);
    }

    public Order getOrderById(String orderId) throws AppException {

        if (Objects.isNull(orderId) || orderId.isEmpty() || isValidUUID(orderId)) {
            throw new AppException(ErrorConstants.ORDER_ID_FORMAT_INVALID);
        }
        Optional<Order> optionalOrder = orderRepository.findById(UUID.fromString(orderId));
        if (optionalOrder.isEmpty()) {
            throw new AppException(ErrorConstants.ORDER_ID_NOT_FOUND);
        }
        return optionalOrder.get();

    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
