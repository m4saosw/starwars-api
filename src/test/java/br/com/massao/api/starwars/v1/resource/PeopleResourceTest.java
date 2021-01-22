package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.service.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(PeopleResource.class)  //  Auto-configure the Spring MVC infrastructure for unit tests
public class PeopleResourceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PeopleService peopleService;


    @BeforeEach
    public void setUp() {
    }

    /**
     * LIST TEST CASES
     */

    @Test
    public void givenPeopleNotFoundWhenGetPeopleThenReturnStatus200() throws Exception {
        // given
        List<PersonModel> peopleModel = Arrays.asList();

        // when
        given(peopleService.list()).willReturn(peopleModel);

        // then
        mvc.perform(get("/v1/people")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenPeopleWhenGetPeopleThenReturnPeopleWithStatus200() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().id(2L).birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();
        List<PersonModel> peopleModel = Arrays.asList(person1, person2);

        // when
        given(peopleService.list()).willReturn(peopleModel);

        // then
        mvc.perform(get("/v1/people")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(person1.getName())))
                .andExpect(jsonPath("$[1].name", is(person2.getName())));
    }


    /**
     * FIND BY ID TEST CASES
     */

    @Test
    public void givenNotFoundWhenFindByIdPersonThenReturnStatus404() throws Exception {
        // given

        // when
        Mockito.when(peopleService.findById(anyLong())).thenThrow(NotFoundException.class);

        // then
        mvc.perform(get("/v1/people/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }


    @Test
    public void givenPersonWhenFindByIdPersonThenReturnPersonWithStatus200() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // when
        given(peopleService.findById(person1.getId())).willReturn(Optional.of(person1));

        // then
        mvc.perform(get("/v1/people/{id}", person1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(person1.getId()));
    }
}