package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

        Optional<PersonModel> person = repository.findById(id);

        if (!person.isPresent()) throw new NotFoundException();

        return person;
    }


    /**
     * @param person
     * @return
     */
    public PersonModel save(@Valid PersonModel person) {
        log.debug("save person={}", person);

        // TODO - refactor - handler exception
        Set<ConstraintViolation<PersonModel>> violations = validator.validate(person);
        if (! violations.isEmpty()) throw new ConstraintViolationException(violations);

        return repository.save(person);
    }

    /**
     *
     * @param id
     * @throws NotFoundException
     */
    public void deleteById(Long id) throws NotFoundException {
        log.debug("deleteById id={}", id);

        try {
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }

    /**
     * @param id
     * @param newPerson
     * @return
     */
    public Optional<PersonModel> modify(Long id, PersonModel newPerson) throws NotFoundException {
        Optional<PersonModel> person = findById(id);

        // Updates current person
        person.get().setName(newPerson.getName());
        person.get().setBirth_year(newPerson.getBirth_year());
        person.get().setGender(newPerson.getGender());
        person.get().setHeight((newPerson.getHeight()));
        person.get().setMass((newPerson.getMass()));
        person.get().setHomeworld((newPerson.getHomeworld()));

        return Optional.of(repository.save(person.get()));
    }
}
