package br.com.fiap.postech.adjt.checkout.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.clients.CartClient;
import br.com.fiap.postech.adjt.checkout.clients.ClearCartClient;
import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.controller.exception.NotFoundException;
import br.com.fiap.postech.adjt.checkout.model.CartItemEntity;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.response.PaymentResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final CartClient cartClient;
    private final ClearCartClient clearCartClient;

    private final String apiKey = "777396e205b7881490af58e82df453333673428889284694abab7dd9";

    @Transactional
    public PaymentResponse processCheckout(UUID consumerId, double amount, String currency,
                                           PaymentRequest.PaymentMethod paymentMethod) {

        // Cria um pedido com status "pending"
        OrderEntity order = createPendingOrder(consumerId, amount, paymentMethod);

        // Cria a requisição de pagamento
        PaymentRequest paymentRequest = new PaymentRequest(order.getOrderId().toString(), amount, currency, paymentMethod);

        // Processa o pagamento de forma assíncrona
        CompletableFuture.runAsync(() -> processPaymentAsync(order, paymentRequest));

        // Limpa o carrinho do consumidor
        clearCart(consumerId);

        // Retorna a resposta de pagamento
        return new PaymentResponse(order.getOrderId().toString(), order.getPaymentStatus());
    }

    private OrderEntity createPendingOrder(UUID consumerId, double amount, PaymentRequest.PaymentMethod paymentMethod) {
        OrderEntity order = new OrderEntity();
        order.setConsumerId(consumerId);
        order.setValue(amount);
        order.setPaymentType(paymentMethod.getType());
        order.setPaymentStatus("pending");

        // Busca os itens do carrinho e associa ao pedido
        List<CartItemEntity> cartItems = fetchCartItems(consumerId);
        order.setItems(cartItems);

        return orderRepository.save(order);
    }

    private void processPaymentAsync(OrderEntity order, PaymentRequest paymentRequest) {
        try {
            // Processa o pagamento via API externa
            PaymentResponse paymentResponse = paymentClient.processPayment(apiKey, paymentRequest);
            order.setPaymentStatus(paymentResponse.getStatus());
        } catch (Exception e) {
            // Em caso de erro, define o status como "declined"
            order.setPaymentStatus("declined");
        } finally {
            // Salva o status atualizado do pedido
            orderRepository.save(order);
        }
    }

	private void clearCart(UUID consumerId) {
		// Limpa o carrinho do consumidor via API de carrinho
		try {
			clearCartClient.clearCart(consumerId);
		} catch (Exception e) {
			throw new NotFoundException("Empty cart: " + consumerId);
		}
	}

    private List<CartItemEntity> fetchCartItems(UUID consumerId) {
        try {
            // Busca os itens do carrinho via API de carrinho
            List<CartResponse> cartResponses = cartClient.consultCart(consumerId);

            // Converte CartResponse para CartItemEntity
            return cartResponses.stream()
                    .map(cartResponse -> new CartItemEntity(cartResponse.getItemId(), cartResponse.getQuantity()))
                    .toList();
//            return null;

        } catch (Exception e) {
            // Lança uma exceção se não for possível buscar os itens do carrinho
            throw new NotFoundException("Cart items not found for consumerId: " + consumerId);
        }
    }

    @Override
    public List<OrderEntity> getOrdersByConsumerId(UUID consumerId) {
        return orderRepository.findByConsumerId(consumerId);
    }

    @Override
    public OrderEntity getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found for orderId: " + orderId));
    }
}