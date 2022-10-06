package com.apifood.curso.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apifood.curso.domain.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

}
