package br.com.fiap.postech.adjt.checkout.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "card")
public class Card {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
	
	private UUID consumerId;
    private String number;
    private String expiration_month;
    private String expiration_year;
    private String cvv;
    private String name;
}
