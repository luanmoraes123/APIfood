package com.apifood.curso.domain.repository;

import org.springframework.stereotype.Repository;

import com.apifood.curso.domain.model.Cidade;

@Repository
public interface CidadeRepository extends CustomJpaRepository<Cidade, Long> {

}
