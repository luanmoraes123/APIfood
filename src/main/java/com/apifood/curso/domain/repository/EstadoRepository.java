package com.apifood.curso.domain.repository;

import org.springframework.stereotype.Repository;

import com.apifood.curso.domain.model.Estado;

@Repository
public interface EstadoRepository extends CustomJpaRepository<Estado, Long> {

}
