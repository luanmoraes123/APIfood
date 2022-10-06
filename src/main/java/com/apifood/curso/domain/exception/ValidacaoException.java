package com.apifood.curso.domain.exception;

import org.springframework.validation.BindingResult;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidacaoException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	BindingResult bindingResult;
	
}
