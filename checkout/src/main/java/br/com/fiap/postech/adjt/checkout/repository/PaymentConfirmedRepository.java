package br.com.fiap.postech.adjt.checkout.repository;

import br.com.fiap.postech.adjt.checkout.entity.payment.PaymentConfirmed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentConfirmedRepository extends JpaRepository<PaymentConfirmed, UUID> {
}
