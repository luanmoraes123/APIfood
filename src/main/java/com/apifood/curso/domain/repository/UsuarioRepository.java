package com.apifood.curso.domain.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.apifood.curso.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);
	
}