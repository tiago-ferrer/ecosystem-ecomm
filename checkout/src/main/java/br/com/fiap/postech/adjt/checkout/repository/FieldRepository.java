package br.com.fiap.postech.adjt.checkout.repository;


import br.com.fiap.postech.adjt.checkout.entity.payment.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}
