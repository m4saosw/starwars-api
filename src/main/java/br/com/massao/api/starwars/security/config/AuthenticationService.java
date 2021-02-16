package br.com.massao.api.starwars.security.config;

import br.com.massao.api.starwars.security.model.UserModel;
import br.com.massao.api.starwars.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserModel> user = repository.findByUsername(username);
		if (user.isPresent()) {
			return user.get();
		}
		
		throw new UsernameNotFoundException("Invalid data!");
	}

}
