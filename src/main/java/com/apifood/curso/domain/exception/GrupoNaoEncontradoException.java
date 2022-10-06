package com.apifood.curso.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public GrupoNaoEncontradoException(String msg) {
		super(msg);
	}
	
	public GrupoNaoEncontradoException(Long grupoId) {
		this(String.format("O grupo com id %d não foi encontrado", grupoId));
	}

}
