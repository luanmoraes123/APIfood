package com.apifood.curso.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;

	public FormaPagamentoNaoEncontradaException(String msg) {
		super(msg);
	}

	public FormaPagamentoNaoEncontradaException(Long formaPagamentoId) {
		this(String.format("Não foi encontrada forma de pagamento com id %d", formaPagamentoId));
	}
}
