package com.apifood.curso.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.apifood.curso.api.model.EnderecoModel;
import com.apifood.curso.domain.model.Endereco;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
		
		enderecoToEnderecoModelTypeMap.<String>addMapping(src -> 
		src.getCidade().getEstado().getNome(), (dist, value) -> 
		dist.getCidade().setEstado(value));
				
		return  modelMapper;
	}
}
