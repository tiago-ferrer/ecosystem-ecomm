package br.com.fiap.postech.adjt.checkout.repository;

import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Order findByOrderId(UUID orderUuid);
    List<Order> findByConsumerId(UUID customerId);
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
}

