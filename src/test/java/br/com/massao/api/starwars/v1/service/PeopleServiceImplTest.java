package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
class PeopleServiceImplTest {
    @Autowired
    private PeopleService peopleService;

    @MockBean
    private PeopleRepository peopleRepository;

    @MockBean
    private SwapiPlanetsService planetsService;


    @BeforeEach
    void setUp() {
        Mockito.when(planetsService.existsPlanetByName(any())).thenReturn(true);
    }

    /**
     * LIST TEST CASES
     */

    @Test
    void givenPeopleWhenListThenReturnPeople() {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().id(2L).birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();
        List<PersonModel> peopleModel = Arrays.asList(person1, person2);

        // prepares mock
        Mockito.when(peopleRepository.findAll()).thenReturn(peopleModel);

        // when
        List<PersonModel> peopleResult = peopleService.list();

        // then
        assertThat(peopleResult).isNotNull();
        assertThat(peopleResult.size()).isEqualTo(2);
        assertThat(peopleResult.contains(person1)).isTrue();
        assertThat(peopleResult.contains(person2)).isTrue();

    }

    @Test
    void givenEmptyWhenListThenReturnEmpty() {
        // given
        List<PersonModel> peopleModel = Arrays.asList();

        // prepares mock
        Mockito.when(peopleRepository.findAll()).thenReturn(peopleModel);

        // when
        List<PersonModel> peopleResult = peopleService.list();

        // then
        assertThat(peopleResult).isNotNull();
        assertThat(peopleResult.size()).isEqualTo(0);
    }


    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenPersonWhenFindByIdThenReturnPerson() throws NotFoundException {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // prepares mock
        Mockito.when(peopleRepository.findById(person1.getId())).thenReturn(Optional.of(person1));

        // when
        Optional<PersonModel> peopleResult = peopleService.findById(person1.getId());

        // then
        assertThat(peopleResult.isPresent()).isTrue();
        assertThat(peopleResult.get()).isEqualTo(person1);
    }

    @Test()
    void givenNotFoundWhenFindByIdThenThrowsNotFoundException() {
        // given
        // prepares mock
        Mockito.when(peopleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when/then
        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> peopleService.findById(999L));
    }


    /**
     * CREATE TEST CASES
     */

    @Test
    void givenPersonWhenCreateThenReturnPerson() {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // prepares mock
        Mockito.when(peopleRepository.save(person1)).thenReturn(person1);

        // when
        PersonModel personResult = peopleService.save(person1);

        // then
        assertThat(personResult).isNotNull();
        assertThat(personResult).isEqualTo(person1);
    }

    @Test
    // TODO - melhorar manipulacao de erro - quando invalido, excecao ou null lancar excecao
    void givenInvalidPersonWhenCreateThenThrowsConstraintViolationException() {
        // given
        PersonModel person1 = PersonModel.builder().build();

        // prepares mock
        Mockito.when(peopleRepository.save(person1)).thenReturn(null);

        // when / then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> peopleService.save(person1));
    }

    @Test
    // TODO - melhorar manipulacao de erro - quando invalido, excecao ou null lancar excecao
    void givenInvalidHomeworldInPersonWhenCreateThenThrowsIllegalArgumentException() {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // prepares mock
        Mockito.when(planetsService.existsPlanetByName(any())).thenReturn(false);
        Mockito.when(peopleRepository.save(person1)).thenReturn(null);

        // when / then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> peopleService.save(person1));
    }


    /**
     * DELETE BY ID TEST CASES
     */

    @Test
    void givenPersonWhenDeleteByIdThenDelete() throws NotFoundException {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // prepares mock
        Mockito.doNothing().when(peopleRepository).deleteById(person1.getId());

        // when
        peopleService.deleteById(person1.getId());

        // then
        verify(peopleRepository).deleteById(person1.getId());
    }

    @Test()
    void givenNotFoundWhenDeleteByIdThenThrowsNotFoundException() {
        // given
        // prepares mock
        Mockito.doThrow(EmptyResultDataAccessException.class).when(peopleRepository).deleteById(anyLong());

        // when/then
        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> peopleService.deleteById(999L));
    }


    /**
     * MODIFY TEST CASES
     */

    @Test
    void givenPersonWhenModifyThenModify() throws NotFoundException {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // prepares mock
        Mockito.when(peopleRepository.findById(any())).thenReturn(Optional.of(person1));
        Mockito.when(peopleRepository.save(any())).thenReturn(person1);

        // when
        peopleService.update(person1.getId(), person1);

        // then
        verify(peopleRepository).findById(any());
        verify(peopleRepository).save(any());
    }

    @Test()
    void givenInvalidPersonIdWhenModifyThenThrowsNotFoundException() {
        // given
        // prepares mock
        Mockito.when(peopleRepository.findById(anyLong())).thenReturn(Optional.empty());
        PersonModel model = PersonModel.builder().build();

        // when/then
        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> peopleService.update(9999L, model));
    }

    @Test()
    void givenInvalidAttributesInPersonWhenModifyThenThrowsConstraintViolationException() {
        // given
        // prepares mock
        PersonModel person1 = PersonModel.builder().id(1L).build();
        Mockito.when(peopleRepository.findById(any())).thenReturn(Optional.of(person1));

        // when/then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> peopleService.update(9999L, person1));
    }

    @Test()
    void givenInvalidHomeworldInPersonWhenModifyThenThrowsIllegalArgumentException() {
        // given
        // prepares mock
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        person1.setHomeworld("");
        Mockito.when(peopleRepository.findById(any())).thenReturn(Optional.of(person1));

        // when/then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> peopleService.update(9999L, person1));
    }

    /**
     * TestConfiguration guarantee this bean is only for test scope
     */
    @TestConfiguration
    static class PeopleServiceTestContextConfiguration {
        @Bean
        PeopleService peopleService() {
            return new PeopleServiceImpl();

        }


        @Bean
        Validator validator() {
            return new LocalValidatorFactoryBean();
        }
    }

    //  https://www.sisense.com/blog/rest-api-testing-strategy-what-exactly-should-you-test/
}