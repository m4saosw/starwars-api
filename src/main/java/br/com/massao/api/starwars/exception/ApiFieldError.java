package br.com.massao.api.starwars.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApiFieldError {
	@JsonProperty("field")
	private String field;

	@JsonProperty("message")
	private String message;

	public ApiFieldError(String field, String message) {
		this.field = field;
		this.message = message;
	}
}
