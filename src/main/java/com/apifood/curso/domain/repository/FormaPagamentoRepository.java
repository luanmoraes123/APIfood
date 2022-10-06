package com.apifood.curso.domain.repository;

import org.springframework.stereotype.Component;

import com.apifood.curso.domain.model.FormaPagamento;

@Component
public interface FormaPagamentoRepository extends CustomJpaRepository<FormaPagamento, Long>{

}
