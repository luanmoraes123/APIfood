package com.lmfood.curso.domain.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(nullable = false)
	private String nome;

	@JoinColumn(nullable = false)
	private String descricao;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;

	@JoinColumn(nullable = false)
	private BigDecimal preco;

	@JoinColumn(nullable = false)
	private boolean ativo;
}
