package br.com.massao.api.starwars.security.validation;

public class ErrorApi {
	
	private String field;
	private String error;
	
	public ErrorApi(String field, String error) {
		this.field = field;
		this.error = error;
	}

	public String getField() {
		return field;
	}

	public String getError() {
		return error;
	}
	
	

}
