package com.apifood.curso.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apifood.curso.api.model.input.ProdutoInput;
import com.apifood.curso.domain.model.Produto;

@Component
public class ProdutoInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Produto toDomainObject(ProdutoInput produtoInput) {
		return modelMapper.map(produtoInput, Produto.class);
	}
	
	public void copyToDomainObject(Produto produto, ProdutoInput produtoInput) {
		modelMapper.map(produtoInput, produto);
	}
}
