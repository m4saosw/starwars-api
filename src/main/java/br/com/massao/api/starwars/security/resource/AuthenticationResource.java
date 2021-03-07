package br.com.massao.api.starwars.security.resource;

import br.com.massao.api.starwars.security.config.TokenService;
import br.com.massao.api.starwars.security.dto.Token;
import br.com.massao.api.starwars.security.form.LoginForm;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;

	/**
	 * Generate an authentication token
	 * @param form
	 * @return
	 */
	@ApiOperation(value = "Generate an authentication token", notes = "Insert a generated token into a protected resource")
	@PostMapping
	@ApiResponses(value={
			@ApiResponse(code=200, message="Success", response = Token.class),
			@ApiResponse(code=400, message="Bad Request"),
			@ApiResponse(code=500, message="Internal Server Error")
	})
	public ResponseEntity<Token> authenticate(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken loginData = form.converter();
		
		try {
			Authentication authentication = authManager.authenticate(loginData);
			String token = tokenService.tokenFrom(authentication);

			return ResponseEntity.ok(new Token(token, "Bearer"));

		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
