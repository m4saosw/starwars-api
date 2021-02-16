package br.com.massao.api.starwars.security.config;

import br.com.massao.api.starwars.security.model.UserModel;
import br.com.massao.api.starwars.security.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationByTokenFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	private UserRepository repository;

	public AuthenticationByTokenFilter(TokenService tokenService, UserRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = tokenFrom(request);
		if (tokenService.isValidToken(token)) {
			authenticateClient(token);
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticateClient(String token) {
		Long idUser = tokenService.getIdUser(token);
		UserModel userModel = repository.findById(idUser).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userModel, null, userModel.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String tokenFrom(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		return token.substring(7, token.length());
	}

}
