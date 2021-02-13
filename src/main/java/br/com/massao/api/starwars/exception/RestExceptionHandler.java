package br.com.massao.api.starwars.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Adaptado de https://www.toptal.com/java/spring-boot-rest-api-error-handling
 * 
 * @author Massao
 *
 */

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	// Classe para internacionalizacao das mensagens
	@Autowired
	MessageSource messageSource;

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(DateTimeException.class)
	public ResponseEntity<Object> handleInvalidDate(Exception ex) {
		String message = "Invalid date format";
		log.debug("handleInvalidDate exception={}", ex.getMessage());

		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, ex));
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Object> handleHttpClientError(Exception ex) {
		String message = "Internal service error";
		log.debug("handleHttpClientError exception={}", ex.getMessage());

		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, ex));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(Exception ex) {
		String message = "Invalid arguments in body";
		log.debug("handleHttpClientError exception={}", ex.getMessage());

		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, ex));
	}

	/**
	 * Request param handler
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = "Missing parameter";
		log.debug("handleMissingServletRequestParameter exception={}", ex.getMessage());

		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, ex));
	}

	/**
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = "Invalid Json Request";
		log.debug("handleHttpMessageNotReadable exception={}", ex.getMessage());

		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, ex));
	}

	/**
	 * Capture another exceptions
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String message = "Internal exception";
		log.debug("handleExceptionInternal exception={}", ex.getMessage());

		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, ex));
	}




	/**
	 * Trata Request body invalido em requisição POST
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		//return super.handleMethodArgumentNotValid(ex, headers, status, request);
		String message = "Invalid arguments in body";
		log.debug("handleMethodArgumentNotValid exception={}", ex);

		List<ApiFieldError> apiFieldErrors = new ArrayList<>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		fieldErrors.forEach(e -> {
			String msg = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ApiFieldError apiFieldError = new ApiFieldError(e.getField(), msg);
			apiFieldErrors.add(apiFieldError);
		});

		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, ex, apiFieldErrors));
	}


	/**
	 * Default handler
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		String message = "Internal exception at " + request.toString();
		log.debug("handleAll exception={}", ex.getMessage());

		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, ex));
	}
}
