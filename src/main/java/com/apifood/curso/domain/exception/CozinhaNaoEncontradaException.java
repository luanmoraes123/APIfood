package com.apifood.curso.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;

	public CozinhaNaoEncontradaException(String msg) {
		super(msg);
	}
	
	public CozinhaNaoEncontradaException(Long cozinhaId) {
		this(String.format("Não foi encontrada cozinha com id %d", cozinhaId));
	}
}
