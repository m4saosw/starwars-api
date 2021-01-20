package br.com.massao.api.starwars.v1.repository;


import br.com.massao.api.starwars.model.PersonModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @DataJpaTest provides some standard setup needed for testing the persistence layer:
 * <p>
 * configuring H2, an in-memory database
 * setting Hibernate, Spring Data, and the DataSource
 * performing an @EntityScan
 * turning on SQL logging
 */
@DataJpaTest
public class PeopleRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PeopleRepository peopleRepository;


    @BeforeEach
    public void setUp() {
    }

    @Test
    public void whenListThenReturnPeople() {
        // given
        peopleRepository.deleteAll();

        PersonModel person1 = PersonModel.builder().birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();

        PersonModel person1Persisted = (PersonModel) entityManager.persist(person1);
        PersonModel person2Persisted = (PersonModel) entityManager.persist(person2);
        entityManager.flush();

        // when
        List<PersonModel> people = peopleRepository.findAll();

        // then
        assertThat(people).isNotEmpty();
        assertThat(people.size()).isEqualTo(2);
        assertThat(people.contains(person1Persisted)).isTrue();
        assertThat(people.contains(person2Persisted)).isTrue();

    }


    // TODO - incluir/ excluir/ alterar
}