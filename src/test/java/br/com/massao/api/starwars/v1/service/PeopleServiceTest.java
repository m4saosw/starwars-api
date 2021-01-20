package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
public class PeopleServiceTest {
    @Autowired
    private PeopleService peopleService;

    @MockBean
    private PeopleRepository peopleRepository;



    @BeforeEach
    public void setUp() {
    }

    @Test
    public void whenListThenReturnPeople() {
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


    /**
     * TestConfiguration guarantee this bean is only for test scope
     */
    @TestConfiguration
    static class PeopleServiceTestContextConfiguration {
        @Bean
        public PeopleService peopleService() {
            return new PeopleService();

        }
    }
}