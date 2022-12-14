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

import com.apifood.curso.api.assembler.EstadoInputDisassembler;
import com.apifood.curso.api.assembler.EstadoModelAssembler;
import com.apifood.curso.api.model.EstadoModel;
import com.apifood.curso.api.model.input.EstadoInput;
import com.apifood.curso.domain.model.Estado;
import com.apifood.curso.domain.repository.EstadoRepository;
import com.apifood.curso.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoContoller {

	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CadastroEstadoService cadastroEstado;
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;
	
	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;

	@GetMapping
	public List<EstadoModel> listar() {
		return estadoModelAssembler.toColletcionModel(estadoRepository.findAll());
	}

	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId) {
		return estadoModelAssembler.toModel(cadastroEstado.buscarOuFalhar(estadoId));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
			return estadoModelAssembler.toModel(cadastroEstado.salvar(estado));
	}

	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {
		
		Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);
		estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
		return estadoModelAssembler.toModel(cadastroEstado.salvar(estadoAtual));
	}

	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
		cadastroEstado.excluir(estadoId);
	}
}
