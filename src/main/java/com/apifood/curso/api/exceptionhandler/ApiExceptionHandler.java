package com.apifood.curso.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.apifood.curso.domain.exception.EntidadeEmUsoException;
import com.apifood.curso.domain.exception.EntidadeNaoEncontradaException;
import com.apifood.curso.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	public static final String ERRO_GENERICO_USUARIO = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir entre em contato com o administrador do sistema.";
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		}
		
		if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		Problem problem = createProblemBuilder(status, detail, problemType)
				.userMessage(ERRO_GENERICO_USUARIO)
				.timestamp(OffsetDateTime.now())
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		
		String path = ex.getPath().stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
		
		String detail = String.format("O campo '%s' não existe. Corrija ou remova essa propriedade e tente novamente", path);
		
		Problem problem = createProblemBuilder(status, detail, problemType)
				.timestamp(OffsetDateTime.now())
				.userMessage(ERRO_GENERICO_USUARIO)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "um ou mais campos estão invalidos, preencha corretamente e tente novamente";
		BindingResult bindingResult = ex.getBindingResult();
		List<Problem.Field> problemField = bindingResult.getFieldErrors().stream()
				.map(fieldError ->{
					String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
				return Problem.Field.builder()
						.name(fieldError.getField())
						.userMessage(message)
						.build();})
				.collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status, detail, problemType)
				.userMessage(detail)
				.timestamp(OffsetDateTime.now())
				.fileds(problemField)
				.build();
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
	        HttpStatus status, WebRequest request) {
	    
	    if (ex instanceof MethodArgumentTypeMismatchException) {
	        return handleMethodArgumentTypeMismatch(
	                (MethodArgumentTypeMismatchException) ex, headers, status, request);
	    }

	    return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
	        MethodArgumentTypeMismatchException ex, HttpHeaders headers,
	        HttpStatus status, WebRequest request) {

	    ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

	    String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
	            + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
	            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

	    Problem problem = createProblemBuilder(status, detail, problemType)
	    		.userMessage(ERRO_GENERICO_USUARIO)
	    		.timestamp(OffsetDateTime.now())
	    		.build();

	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String url = ex.getRequestURL();
		String detail = String.format("O recurso %s, que você tentou acessar, é inexistente. ", url);
		
		Problem problem = createProblemBuilder(status, detail, problemType)
				.userMessage(detail)
				.timestamp(OffsetDateTime.now())
				.build();
		
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
			
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String path = ex.getPath().stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
		String detail = String.format("A propriedade '%s' receceu o valor '%s' "
				+ "que é de um tipo inválido. Corrija e informe um valor do tipo '%s' ",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, detail, problemType)
				.userMessage(ERRO_GENERICO_USUARIO)
				.timestamp(OffsetDateTime.now())
				.build();
			
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(NegocioException.class)
	private ResponseEntity<?> tratarNegocioException(NegocioException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;

		Problem problem = createProblemBuilder(status, ex.getMessage(), problemType)
				.userMessage(ex.getMessage())
				.timestamp(OffsetDateTime.now())
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	private ResponseEntity<?> tratarEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

		Problem problem = createProblemBuilder(status, ex.getMessage(), problemType)
				.userMessage(ex.getMessage())
				.timestamp(OffsetDateTime.now())
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	private ResponseEntity<?> tratarEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

		Problem problem = createProblemBuilder(status, ex.getMessage(), problemType)
				.userMessage(ex.getMessage())
				.timestamp(OffsetDateTime.now())
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(Exception.class)
	private ResponseEntity<?> tratarErroDeSistema(Exception ex, WebRequest request){
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail =ERRO_GENERICO_USUARIO;
		
		ex.printStackTrace();

		Problem problem = createProblemBuilder(status, detail, problemType)
				.userMessage(detail)
				.timestamp(OffsetDateTime.now())
				.build();

		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = Problem.builder()
					.status(status.value())
					.userMessage(ERRO_GENERICO_USUARIO)
					.timestamp(OffsetDateTime.now())
					.title(status.getReasonPhrase())
					.build();
		} else if (body instanceof String) {
			body = Problem.builder()
					.status(status.value())
					.detail((String) body)
					.userMessage(ERRO_GENERICO_USUARIO)
					.timestamp(OffsetDateTime.now())
					.build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, String detail, ProblemType problemType){
		
		return Problem.builder()
				.detail(detail)
				.type(problemType.getType())
				.status(status.value())
				.title(problemType.getTitle());
				
	}
}
