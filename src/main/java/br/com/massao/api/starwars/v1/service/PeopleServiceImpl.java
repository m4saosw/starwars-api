package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Validated
public class PeopleServiceImpl implements PeopleService {

    @Autowired
    private PeopleRepository repository;

    @Autowired
    private Validator validator;

    @Autowired
    private SwapiPlanetsService planetsService;

    /**
     * List all person
     *
     * @return
     */
    // @Cacheable(key = "people")  Nao sera cacheado pois a area de negocio indicou que a lista de pessoas mudara constantemente, teoricamente inviabilizaria a performance do cache
    @Override
    public List<PersonModel> list() {
        log.debug("list");

        return repository.findAll();
    }

    @Override
    public Page<PersonModel> list(Pageable pageable) {
        log.debug("list");

        return repository.findAll(pageable);
    }


    /**
     * Find person by id
     *
     * @param id
     * @return
     * @throws NotFoundException
     */
    @Override
    public Optional<PersonModel> findById(Long id) throws NotFoundException {
        log.debug("findById id={}", id);

        Optional<PersonModel> person = repository.findById(id);

        if (!person.isPresent()) throw new NotFoundException();

        return person;
    }


    /**
     * Save new person
     *
     * @param person
     * @return
     */
    @Transactional
    @Override
    public PersonModel save(@Valid PersonModel person) {
        Instant instant = Instant.now();
        log.debug("save person={}", person);

        // TODO - refactor - handler exception
        Set<ConstraintViolation<PersonModel>> violations = validator.validate(person);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);

        if (!planetsService.existsPlanetByName(person.getHomeworld()))
            throw new IllegalArgumentException("Homeworld is not a valid Planet in StarWars API");
        log.debug("save - existsPlanetByName planet={} elapsedTime={} ms", person.getHomeworld(), Duration.between(instant, Instant.now()).toMillis());

        return repository.save(person);
    }

    /**
     * Delete person by id
     *
     * @param id
     * @throws NotFoundException
     */
    @Transactional
    @Override
    public void deleteById(Long id) throws NotFoundException {
        log.debug("deleteById id={}", id);

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }

    /**
     * Update person
     *
     * @param id
     * @param newPerson
     * @return
     * @throws NotFoundException
     */
    @Transactional
    @Override
    public Optional<PersonModel> update(Long id, PersonModel newPerson) throws NotFoundException {
        Optional<PersonModel> person = findById(id);

        // Updates current person
        person.get().setName(newPerson.getName());
        person.get().setBirthYear(newPerson.getBirthYear());
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
    //@Override
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

        return repository.saveAll(people);
    }
}
