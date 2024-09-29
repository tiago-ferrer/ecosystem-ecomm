package br.com.fiap.postech.adjt.checkout.repository;


import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
