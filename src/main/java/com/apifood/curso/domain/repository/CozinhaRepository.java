package com.apifood.curso.domain.repository;

import org.springframework.stereotype.Repository;

import com.apifood.curso.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {

}
