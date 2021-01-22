package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PeopleService {

    @Autowired
    private PeopleRepository repository;

    /**
     * @return
     */
    public List<PersonModel> list() {
        log.debug("list");

        return repository.findAll();
    }

    /**
     * @param id
     * @return
     */
    // mudar para ResourceNotFoundException ?
    public Optional<PersonModel> findById(Long id) throws NotFoundException {
        log.debug("findById id={}", id);

        Optional<PersonModel> planet = repository.findById(id);

        if (!planet.isPresent()) throw new NotFoundException();

        return planet;
    }


    /**
     * @param person
     * @return
     */
    public PersonModel save(PersonModel person) {
        return repository.save(person);
    }
}
