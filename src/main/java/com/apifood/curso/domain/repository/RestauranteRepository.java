package com.apifood.curso.domain.repository;

import org.springframework.stereotype.Repository;

import com.apifood.curso.domain.model.Restaurante;;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long> {

}
