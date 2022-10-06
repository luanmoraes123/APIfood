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

import com.apifood.curso.api.assembler.CidadeInputDisassembler;
import com.apifood.curso.api.assembler.CidadeModelAssembler;
import com.apifood.curso.api.model.CidadeModel;
import com.apifood.curso.api.model.input.CidadeInput;
import com.apifood.curso.domain.exception.EstadoNaoEncontradoException;
import com.apifood.curso.domain.exception.NegocioException;
import com.apifood.curso.domain.model.Cidade;
import com.apifood.curso.domain.repository.CidadeRepository;
import com.apifood.curso.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@GetMapping
	public List<CidadeModel> listar() {
		return cidadeModelAssembler.toColletcionModel(cidadeRepository.findAll());
	}

	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@PathVariable Long cidadeId) {
		return cidadeModelAssembler.toModel(cadastroCidade.buscarOuFalhar(cidadeId));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {		
			try {
				Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
				return cidadeModelAssembler.toModel(cadastroCidade.salvar(cidade));
			} catch (EstadoNaoEncontradoException e) {
				throw new NegocioException(e.getMessage(), e);				
			}
	}

	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {
		Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

		cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

		try {
			return cidadeModelAssembler.toModel(cadastroCidade.salvar(cidadeAtual));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage());				
		}
	}

	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidade.excluir(cidadeId);
	}
	
	
}
