package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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

    @Autowired
    private SwapiPlanetsService planetsService;

    /**
     * @return
     */
    // TODO - @Cacheable(key = "people")
    public List<PersonModel> list() {
        log.debug("list");

        return repository.findAll();
    }

    /**
     * @param id
     * @return
     */
    //TODO - @Cacheable(value = "person")
    public Optional<PersonModel> findById(Long id) throws NotFoundException {
        log.debug("findById id={}", id);

        Optional<PersonModel> person = repository.findById(id);

        if (!person.isPresent()) throw new NotFoundException();

        //return person;
        return Optional.of(person).orElseThrow(NotFoundException::new);
    }


    /**
     * @param person
     * @return
     */
    public PersonModel save(@Valid PersonModel person) {
        Instant instant = Instant.now();
        log.debug("save person={}", person);

        // TODO - refactor - handler exception
        Set<ConstraintViolation<PersonModel>> violations = validator.validate(person);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);

        if (!planetsService.existsPlanetByName(person.getHomeworld()))
            throw new IllegalArgumentException("Homeworld is not a valid Planet in StarWars API");
        log.debug("save - existsPlanetByName planet={} elapsedTime={} ms", person.getHomeworld(), Duration.between(instant, Instant.now()).toMillis());

        PersonModel save = repository.save(person);
        return save;
    }

    /**
     * @param id
     * @throws NotFoundException
     */
    public void deleteById(Long id) throws NotFoundException {
        log.debug("deleteById id={}", id);

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
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

        // chamada explicita do metodo save devido a validacao do atributo homeworld
        return Optional.of(this.save(person.get()));
    }


    /**
     * Bulk save of person
     *
     * @param people
     * @return
     */
    // Exercitar o conceito de transacao do spring
    // Anotacao Transactional explicita para facilitar entendimento, pois metodo saveall ja e transactional
    // Obs:
    // - transactional + multiplos save, tem o mesmo resultado, isto é, gera rollback em caso de falha
    // - Valid - validacao novamente na camada de servico
    @Transactional
    public List<PersonModel> saveMany(@Valid List<PersonModel> people) {
        Instant instant = Instant.now();
        log.debug("save many person={}", people);

        // TODO - refactor - handler exception
        for (PersonModel person : people) {
            Set<ConstraintViolation<PersonModel>> violations = validator.validate(person);
            if (!violations.isEmpty()) throw new ConstraintViolationException(violations);

            if (!planetsService.existsPlanetByName(person.getHomeworld()))
                throw new IllegalArgumentException("Homeworld is not a valid Planet in StarWars API");
            log.debug("save - existsPlanetByName planet={} elapsedTime={} ms", person.getHomeworld(), Duration.between(instant, Instant.now()).toMillis());
        }

        List<PersonModel> results = repository.saveAll(people);

//        List<PersonModel> results = new ArrayList<>();
//        for (PersonModel person : people) {
//            results.add(repository.save(person));
//        }
//        // repository.saveAll(people);

        return results;
    }
}
