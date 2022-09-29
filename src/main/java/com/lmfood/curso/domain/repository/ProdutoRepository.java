package com.lmfood.curso.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lmfood.curso.domain.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
