package br.com.massao.api.starwars.v1.repository;


import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;


/**
 * @DataJpaTest provides some standard setup needed for testing the persistence layer:
 * <p>
 * configuring H2, an in-memory database
 * setting Hibernate, Spring Data, and the DataSource
 * performing an @EntityScan
 * turning on SQL logging
 */
@DataJpaTest
class PeopleRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PeopleRepository peopleRepository;


    @BeforeEach
    void setUp() {
    }

    @Test
    void givenPeopleWhenListThenReturnPeople() {
        // given
        peopleRepository.deleteAll();

        PersonModel person1 = PersonModel.builder().birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();

        PersonModel person1Persisted = entityManager.persist(person1);
        PersonModel person2Persisted = entityManager.persist(person2);
        entityManager.flush();

        // when
        List<PersonModel> people = peopleRepository.findAll();

        // then
        assertThat(people).isNotEmpty();
        assertThat(people.size()).isEqualTo(2);
        assertThat(people.contains(person1Persisted)).isTrue();
        assertThat(people.contains(person2Persisted)).isTrue();

    }


    @Test
    void givenPersonWhenFindByIdThenReturnPerson() {
        // given
        peopleRepository.deleteAll();

        PersonModel person1 = PersonModel.builder().birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();

        Long idGenerated1 = (Long) entityManager.persistAndGetId(person1);
        entityManager.flush();

        // when
        Optional<PersonModel> personFound = peopleRepository.findById(idGenerated1);

        // then
        assertThat(personFound.isPresent()).isTrue();
        assertThat(personFound.get().getId()).isEqualTo(idGenerated1);
    }

    @Test
    void givenPersonWhenCreateThenSave() {
        // given
        PersonModel person1 = PersonModel.builder().birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // when
        PersonModel personFound = peopleRepository.saveAndFlush(person1);

        // then
        assertThat(personFound).isNotNull();
        assertThat(personFound).isEqualTo(person1);
    }

    @Test
    void givenInvalidPersonWhenCreateThenThrowsRuntimeException() {
        // given
        PersonModel person1 = PersonModel.builder().build();

        // when / then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> peopleRepository.saveAndFlush(person1));
    }


    @Test
    void givenPersonWhenDeleteByIdThenDelete() {
        // given
        peopleRepository.deleteAll();

        PersonModel person1 = PersonModel.builder().birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();

        Long idGenerated1 = (Long) entityManager.persistAndGetId(person1);
        Long idGenerated2 = (Long) entityManager.persistAndGetId(person2);
        entityManager.flush();
        long totalOfRecords = peopleRepository.count();

        // when
        peopleRepository.deleteById(idGenerated1);

        // then
        assertThat(peopleRepository.findById(idGenerated1).isPresent()).isFalse();
        assertThat(peopleRepository.count()).isEqualTo(totalOfRecords - 1);
    }


    @Test
    void givenInvalidPersonWhenDeleteByIdThenThrowsEmptyResultDataAccessException() {
        // given

        // when / then
        Assertions.assertThatExceptionOfType(EmptyResultDataAccessException.class).isThrownBy(
                () -> peopleRepository.deleteById(9999L));
    }

    // TODO - alterar


    @Test
    void givenPersonWhenModifyByIdThenModify() {
        // given
        peopleRepository.deleteAll();

        PersonModel person1 = PersonModel.builder().birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();

        Long idGenerated1 = (Long) entityManager.persistAndGetId(person1);
        entityManager.flush();

        // when
        PersonModel person = peopleRepository.findById(idGenerated1).get();
        person.setName(person2.getName());
        person.setHomeworld(person2.getHomeworld());
        person.setMass(person2.getMass());
        person.setHeight(person2.getHeight());
        person.setGender(person2.getGender());
        person.setBirth_year(person2.getBirth_year());

        peopleRepository.save(person);


        // then
        PersonModel model = peopleRepository.findById(idGenerated1).get();
        assertThat(model).isNotNull();
        assertThat(model.getName()).isEqualTo(person2.getName());
        assertThat(model.getHomeworld()).isEqualTo(person2.getHomeworld());
        assertThat(model.getMass()).isEqualTo(person2.getMass());
        assertThat(model.getHeight()).isEqualTo(person2.getHeight());
        assertThat(model.getGender()).isEqualTo(person2.getGender());
        assertThat(model.getBirth_year()).isEqualTo(person2.getBirth_year());
    }

}