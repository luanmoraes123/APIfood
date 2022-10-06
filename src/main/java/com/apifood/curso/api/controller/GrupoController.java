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

import com.apifood.curso.api.assembler.GrupoInputDisassembler;
import com.apifood.curso.api.assembler.GrupoModelAssembler;
import com.apifood.curso.api.model.GrupoModel;
import com.apifood.curso.api.model.input.GrupoInput;
import com.apifood.curso.domain.model.Grupo;
import com.apifood.curso.domain.repository.GrupoRepository;
import com.apifood.curso.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private CadastroGrupoService cadastroGrupo;
	
	@Autowired
	private GrupoModelAssembler grupoAssembler;
	
	@Autowired
	private GrupoRepository gruporepository;
	
	@Autowired
	private GrupoInputDisassembler grupoDisassembler;
	
	@GetMapping("/{grupoId}")
	public GrupoModel buscar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		return grupoAssembler.toModel(grupo);
	}
	
	@GetMapping
	public List<GrupoModel> listar(){
		return grupoAssembler.toCollectionModel(gruporepository.findAll());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel salvar (@RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupo = grupoDisassembler.toDomainObject(grupoInput);
		return grupoAssembler.toModel(cadastroGrupo.salvar(grupo));
	}
	
	@PutMapping("/{grupoId}")
	public GrupoModel alterar(@RequestBody @Valid GrupoInput grupoInput, @PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		grupoDisassembler.copyToDomainObject(grupoInput, grupo);
		return grupoAssembler.toModel(cadastroGrupo.salvar(grupo));
	}
	
	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void escluir(@PathVariable Long grupoId) {
		cadastroGrupo.excluir(grupoId);
	}
}
