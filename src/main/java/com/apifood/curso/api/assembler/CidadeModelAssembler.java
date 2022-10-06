package com.apifood.curso.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apifood.curso.api.model.CidadeModel;
import com.apifood.curso.domain.model.Cidade;

@Component
public class CidadeModelAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public CidadeModel toModel(Cidade cidade) {
		return modelMapper.map(cidade, CidadeModel.class);
	}
	
	public List<CidadeModel> toColletcionModel(List<Cidade> cidades) {
		return cidades.stream()
				.map(cidade -> toModel(cidade))
				.collect(Collectors.toList());
		
	}
}
