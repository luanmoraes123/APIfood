package com.apifood.curso.domain.exception;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public EstadoNaoEncontradoException(String msg) {
		super(msg);
	}
	
	public EstadoNaoEncontradoException(Long estadoId) {
		this(String.format("Não foi encontrado estado com id %d", estadoId));
	}
}
