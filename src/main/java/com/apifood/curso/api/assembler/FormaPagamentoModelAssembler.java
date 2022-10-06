package com.apifood.curso.api.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apifood.curso.api.model.FormaPagamentoModel;
import com.apifood.curso.domain.model.FormaPagamento;

@Component
public class FormaPagamentoModelAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public FormaPagamentoModel toModel(FormaPagamento formaPagamento) {
		return modelMapper.map(formaPagamento, FormaPagamentoModel.class);
	}
	
	public FormaPagamento toDomainObject(FormaPagamentoModel formaPagamentoModel) {
		return modelMapper.map(formaPagamentoModel, FormaPagamento.class);
	}

	public List<FormaPagamentoModel> toCollectionModel(Collection<FormaPagamento> todasFormasPagamentos) {
		return todasFormasPagamentos.stream().map(formaPagamento -> toModel(formaPagamento))
				.collect(Collectors.toList());
	}
}
