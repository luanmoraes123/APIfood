package com.apifood.curso.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apifood.curso.api.assembler.CozinhaInputDisassembler;
import com.apifood.curso.api.assembler.CozinhaModelAssembler;
import com.apifood.curso.api.model.CozinhaModel;
import com.apifood.curso.api.model.input.CozinhaInput;
import com.apifood.curso.domain.model.Cozinha;
import com.apifood.curso.domain.repository.CozinhaRepository;
import com.apifood.curso.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;

	@GetMapping
	public List<CozinhaModel> listar() {
		return cozinhaModelAssembler.toColletcionModel(cozinhaRepository.findAll());
	}

	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		return cozinhaModelAssembler.toModel(cadastroCozinha.buscarOuFalhar(cozinhaId));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
		return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));		

	}

	@PutMapping("/{cozinhaId}")
	public CozinhaModel alterar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
		

		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		return cozinhaModelAssembler.toModel(cozinhaRepository.save(cozinhaAtual));
	}

	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long cozinhaId) {
		cadastroCozinha.excluir(cozinhaId);
	}
}
