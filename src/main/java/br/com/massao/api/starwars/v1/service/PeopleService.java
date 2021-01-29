package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Validated
// TODO - usar interfaces
public class PeopleService {

    @Autowired
    private PeopleRepository repository;

    @Autowired
    private Validator validator;

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
    public PersonModel save(@Valid PersonModel person) {
        // TODO - refactor - handler exception
        Set<ConstraintViolation<PersonModel>> violations = validator.validate(person);
        if (! violations.isEmpty()) throw new ConstraintViolationException(violations);

        return repository.save(person);
    }
}
