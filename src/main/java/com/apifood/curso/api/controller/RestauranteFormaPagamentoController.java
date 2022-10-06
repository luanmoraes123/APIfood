package com.apifood.curso.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apifood.curso.api.assembler.FormaPagamentoModelAssembler;
import com.apifood.curso.api.model.FormaPagamentoModel;
import com.apifood.curso.domain.model.Restaurante;
import com.apifood.curso.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {

	

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoAssembler;
	
	@GetMapping
	public List<FormaPagamentoModel> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		return formaPagamentoAssembler.toCollectionModel(restaurante.getFormasPagamento());
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){
		cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);		
	}
	
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){
		cadastroRestaurante.adicionarFormaPagamento(restauranteId, formaPagamentoId);		
	}
	
	
}
