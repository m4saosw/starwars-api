package br.com.massao.api.starwars.security.repository;

import br.com.massao.api.starwars.security.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
	
	Optional<UserModel> findByUsername(String username);

}
