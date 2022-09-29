package com.lmfood.curso.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.lmfood.curso.domain.exception.EntidadeEmUsoException;
import com.lmfood.curso.domain.exception.EntidadeNaoEncontradaException;
import com.lmfood.curso.domain.model.Cozinha;
import com.lmfood.curso.domain.model.Restaurante;
import com.lmfood.curso.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	public Restaurante salvar(Restaurante restaurante) {
		System.out.println(restaurante.getNome());
		System.out.println(restaurante.getCozinha());
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);

		restaurante.setCozinha(cozinha);
		return restauranteRepository.save(restaurante);
	}

	public void excluir(Long restauranteId) {
		try {
			restauranteRepository.deleteById(restauranteId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não foi encontrado restaurante com id %d", restauranteId));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("O restaurante com id %d esta em uso", restauranteId));
		}
	}

	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId).orElseThrow(() -> new EntidadeNaoEncontradaException(
				String.format("Não foi encontrado restaurante com id %d", restauranteId)));
	}

}
