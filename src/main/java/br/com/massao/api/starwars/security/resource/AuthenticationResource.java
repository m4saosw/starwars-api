package br.com.massao.api.starwars.security.resource;

import br.com.massao.api.starwars.security.config.TokenService;
import br.com.massao.api.starwars.security.dto.TokenDto;
import br.com.massao.api.starwars.security.form.LoginForm;
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

	@PostMapping
	public ResponseEntity<TokenDto> authenticate(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken loginData = form.converter();
		
		try {
			Authentication authentication = authManager.authenticate(loginData);
			String token = tokenService.tokenFrom(authentication);

			return ResponseEntity.ok(new TokenDto(token, "Bearer"));

		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
