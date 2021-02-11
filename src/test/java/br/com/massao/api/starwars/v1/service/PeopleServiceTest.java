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
public class PeopleServiceTest {
    @Autowired
    private PeopleService peopleService;

    @MockBean
    private PeopleRepository peopleRepository;



    @BeforeEach
    public void setUp() {
    }

    /**
     * LIST TEST CASES
     */

    @Test
    public void givenPeopleWhenListThenReturnPeople() {
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
    public void givenEmptyWhenListThenReturnEmpty() {
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
    public void givenPersonWhenFindByIdThenReturnPerson() throws NotFoundException {
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
    public void givenNotFoundWhenFindByIdThenThrowsNotFoundException() {
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
    public void givenPersonWhenCreateThenReturnPerson() {
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
    public void givenInvalidPersonWhenCreateThenThrowsConstraintViolationException() {
        // given
        PersonModel person1 = PersonModel.builder().build();

        // prepares mock
        Mockito.when(peopleRepository.save(person1)).thenReturn(null);

        // when / then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> peopleService.save(person1));
    }


    /**
     * DELETE BY ID TEST CASES
     */

    @Test
    public void givenPersonWhenDeleteByIdThenDelete() throws NotFoundException {
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
    public void givenNotFoundWhenDeleteByIdThenThrowsNotFoundException() {
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
    public void givenPersonWhenModifyThenModify() throws NotFoundException {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // prepares mock
        Mockito.when(peopleRepository.findById(any())).thenReturn(Optional.of(person1));
        Mockito.when(peopleRepository.save(any())).thenReturn(person1);

        // when
        peopleService.modify(person1.getId(), person1);

        // then
        verify(peopleRepository).findById(any());
        verify(peopleRepository).save(any());
    }

    @Test()
    public void givenInvalidPersonIdWhenModifyThenThrowsNotFoundException() {
        // given
        // prepares mock
        Mockito.when(peopleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        PersonModel model = PersonModel.builder().build();

        // when/then
        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> peopleService.modify(9999L, model));
    }


    /**
     * TestConfiguration guarantee this bean is only for test scope
     */
    @TestConfiguration
    static class PeopleServiceTestContextConfiguration {
        @Bean
        public PeopleService peopleService() {
            return new PeopleService();

        }


        @Bean
        public Validator validator() {
            return new LocalValidatorFactoryBean();
        }
    }

    //  https://www.sisense.com/blog/rest-api-testing-strategy-what-exactly-should-you-test/
}