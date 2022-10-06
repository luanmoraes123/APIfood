package com.apifood.curso.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public ProdutoNaoEncontradoException(String msg) {
		super(msg);
	}
	
	public ProdutoNaoEncontradoException(Long produtoId, Long restauranteId) {
		this(String.format("Não existe um cadastro de produto de codigo "
				+ "%d para o restaurante de codigo %d",produtoId, restauranteId));
	}

}
