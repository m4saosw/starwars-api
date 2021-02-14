package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
//@Validated
public interface PeopleService {
    /**
     * List all person
     *
     * @return
     */
    List<PersonModel> list();

    /**
     * List all person
     *
     * @return
     */
    Page<PersonModel> list(Pageable pageable);

    /**
     * Find person by id
     *
     * @param id
     * @return
     * @throws NotFoundException
     */
    Optional<PersonModel> findById(Long id) throws NotFoundException;

    /**
     * Save new person
     *
     * @param person
     * @return
     */
    @Transactional
    PersonModel save(@Valid PersonModel person);

    /**
     * Delete person by id
     *
     * @param id
     * @throws NotFoundException
     */
    @Transactional
    void deleteById(Long id) throws NotFoundException;

    /**
     * Update person
     *
     * @param id
     * @param newPerson
     * @return
     * @throws NotFoundException
     */
    @Transactional
    Optional<PersonModel> update(Long id, PersonModel newPerson) throws NotFoundException;

    /**
     * Bulk save of person
     *
     * @param people
     * @return
     */
    @Transactional
    List<PersonModel> saveMany(@Valid List<PersonModel> people);
}
