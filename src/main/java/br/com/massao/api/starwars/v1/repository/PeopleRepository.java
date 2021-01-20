package br.com.massao.api.starwars.v1.repository;

import br.com.massao.api.starwars.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<PersonModel, Long> {


}
