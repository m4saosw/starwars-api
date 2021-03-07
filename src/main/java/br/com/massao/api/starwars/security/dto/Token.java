package br.com.massao.api.starwars.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

	@JsonProperty("token")
	private String token;

	@JsonProperty("type")
	private String type;

	public Token(String token, String type) {
		this.token = token;
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public String getType() {
		return type;
	}

}
