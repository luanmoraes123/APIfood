package com.apifood.curso.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.apifood.curso.domain.exception.EntidadeEmUsoException;
import com.apifood.curso.domain.exception.FormaPagamentoNaoEncontradaException;
import com.apifood.curso.domain.model.FormaPagamento;
import com.apifood.curso.domain.repository.FormaPagamentoRepository;

@Service
public class CadastroFormaPagamentoService {

	@Autowired
	FormaPagamentoRepository formaPagamentoRepository;

	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formaPagamentoRepository.save(formaPagamento);
	}

	public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
		return formaPagamentoRepository.findById(formaPagamentoId)
				.orElseThrow(() -> new FormaPagamentoNaoEncontradaException(formaPagamentoId));
	}

	@Transactional
	public void excluir(Long formaPagamentoId) {
		FormaPagamento formaPagamento = buscarOuFalhar(formaPagamentoId);

		try {
			formaPagamentoRepository.delete(formaPagamento);
			formaPagamentoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("A forma de pagamento com id %d esta em uso", formaPagamentoId));
		}
	}
}
