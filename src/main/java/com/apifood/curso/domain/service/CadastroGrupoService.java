package com.apifood.curso.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.apifood.curso.domain.exception.EntidadeEmUsoException;
import com.apifood.curso.domain.exception.GrupoNaoEncontradoException;
import com.apifood.curso.domain.model.Grupo;
import com.apifood.curso.domain.model.Permissao;
import com.apifood.curso.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService {
	
	private static final String MSG_GRUPO_EM_USO 
    = "Grupo de código %d não pode ser removido, pois está em uso";

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroPermissaoService cadastroPermissao;
	
	
	public Grupo buscarOuFalhar(Long grupoId) {
		return grupoRepository.findById(grupoId).orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
	}
	
	public Grupo salvar(Grupo grupo) {
		return grupoRepository.save(grupo);
	}
	
	@Transactional
	public void excluir(Long grupoId) {
		try {
			grupoRepository.deleteById(grupoId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
		} catch (EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradoException(grupoId);
		}
	}
	
	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
	    Grupo grupo = buscarOuFalhar(grupoId);
	    
		Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);
	    
	    grupo.removerPermissao(permissao);
	}

	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
	    Grupo grupo = buscarOuFalhar(grupoId);
	    Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);
	    
	    grupo.adicionarPermissao(permissao);
	}
}
