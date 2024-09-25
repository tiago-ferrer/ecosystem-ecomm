package br.com.fiap.postech.adjt.cart.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
  @Id
  private UUID consumerId;
  
  @OneToMany(mappedBy = "itemId")
  private List<ItemCartEntity> itens;
  
}
