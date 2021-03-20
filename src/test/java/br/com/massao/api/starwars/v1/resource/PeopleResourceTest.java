package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.StartWarsApiApplication;
import br.com.massao.api.starwars.dto.Person;
import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.service.PeopleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(PeopleResource.class)  //  Auto-configure the Spring MVC infrastructure for unit tests
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Com o WebMvcTest nao estava funcionando a configuracao de segurança
//@ContextConfiguration(classes={StartWarsApiApplication.class, SecurityConfig.class, TokenService.class})
@ContextConfiguration(classes={StartWarsApiApplication.class })
@AutoConfigureMockMvc
class PeopleResourceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PeopleService peopleService;
//
//    @Autowired
//    private WebApplicationContext context;


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @BeforeEach
//    void setUp() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity(springSecurityFilterChain))
//                .build();
//        //
//    }

    /**
     * LIST TEST CASES
     */

    @Test
    //@DisplayName("Test list not found returning 200")
    //@WithMockUser(value = "userAdmin1", password = "123456", authorities = "ADMIN")
    void givenPeopleNotFoundWhenGetPeopleThenReturnStatus200() throws Exception {
        // given
        List<PersonModel> peopleModel = Arrays.asList();
        Page<PersonModel> peoplePageModel = new PageImpl<>(peopleModel);

        // when
        given(peopleService.list(any(Pageable.class))).willReturn(peoplePageModel);

        // then
        mvc.perform(get("/v1/people")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$", hasSize(0)));
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    void givenPeopleWhenGetPeopleThenReturnPeopleWithStatus200() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().id(2L).birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();
        List<PersonModel> peopleModel = Arrays.asList(person1, person2);
        Page<PersonModel> peoplePageModel = new PageImpl<>(peopleModel);

        // when
        given(peopleService.list(any(Pageable.class))).willReturn(peoplePageModel);

        // then
        mvc.perform(get("/v1/people")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$['content']", hasSize(2)))
                .andExpect(jsonPath("$.last", is (true))) // com paginacao
                .andExpect(jsonPath("$.totalPages", is(1))) // com paginacao
                .andExpect(jsonPath("$.content[0].name", is(person1.getName())))
                .andExpect(jsonPath("$.content[1].name", is(person2.getName())));
    }

    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenNotFoundWhenFindByIdPersonThenReturnStatus404() throws Exception {
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
    void givenPersonWhenFindByIdPersonThenReturnPersonWithStatus200() throws Exception {
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


    /**
     * CREATE TEST CASES
     */

    @Test
    @WithMockUser(value = "user1", password = "123456", authorities = "ROLE_USER")
    void givenPersonWhenCreatePersonThenReturnLocationWithStatus201() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // when
        given(peopleService.save(any(PersonModel.class))).willReturn(person1);

        // then
        //String location = String.format("starwars-api/v1/people/%s", person1.getId());
        String jsonObject = asJsonString(new Person(person1));

        mvc.perform(post("/v1/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                //.andExpect(header().string("location", location)); // TODO - validar conteudo location
                .andExpect(header().exists("location"));
    }

    @Test
    @WithMockUser(value = "user1", password = "123456", authorities = "ROLE_USER")
    void givenInvalidPersonWhenCreatePersonThenReturnStatus400() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().build();

        // when
        given(peopleService.save(any(PersonModel.class))).willReturn(person1);

        // then
        String jsonObject = asJsonString(new Person(person1));

        mvc.perform(post("/v1/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isBadRequest());
                //.andExpect(content().string("")); TODO - fazer validacao do objeto de erro retornado no body
    }


    /**
     * DELETE BY ID TEST CASES
     */

    @Test
    @WithMockUser(value = "userAdmin1", password = "123456", authorities = "ROLE_ADMIN")
    void givenNotFoundWhenDeleteByIdPersonThenReturnStatus404() throws Exception {
        // given

        // when
        Mockito.doThrow(NotFoundException.class).when(peopleService).deleteById(anyLong());

        // then
        mvc.perform(delete("/v1/people/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(value = "userAdmin1", password = "123456", authorities = "ROLE_ADMIN")
    void givenPersonWhenDeleteByIdPersonThenDeleteAndReturnStatus204() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // when
        //given(peopleService.deleteById(person1.getId())).willReturn();

        // then
        mvc.perform(delete("/v1/people/{id}", person1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }


    /**
     * UPDATE TEST CASES
     */

    @Test
    @WithMockUser(value = "user1", password = "123456", authorities = "ROLE_USER")
    void givenNotFoundWhenUpdatePersonThenReturnStatus404() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();

        // when
        Mockito.when(peopleService.update(anyLong(), any())).thenThrow(NotFoundException.class);

        // then
        String jsonObject = asJsonString(new Person(person1));
        mvc.perform(put("/v1/people/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(value = "user1", password = "123456", authorities = "ROLE_USER")
    void givenPersonWhenUpdateThenReturnPersonWithStatus200() throws Exception {
        // given
        PersonModel person1 = PersonModel.builder().id(1L).birth_year("XFDFD").gender("male").height(123).homeworld("terra").mass(50).name("person1").build();
        PersonModel person2 = PersonModel.builder().birth_year("11111").gender("female").height(123).homeworld("terra").mass(100).name("PERSON2").build();

        // when
        given(peopleService.update(anyLong(), any())).willReturn(Optional.of(person2));

        // then
        String jsonObject = asJsonString(new Person(person2));
        mvc.perform(put("/v1/people/{id}", person1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(person2.getName()))
                .andExpect(jsonPath("$.height").value(person2.getHeight()))
                .andExpect(jsonPath("$.mass").value(person2.getMass()))
                .andExpect(jsonPath("$.birth_year").value(person2.getBirth_year()))
                .andExpect(jsonPath("$.gender").value(person2.getGender()));
                //.andExpect(jsonPath("$.homeworld").value(person2.getHomeworld()));
    }


    // se for excecao e der erro, validar o content type do erro
    // https://blog.runscope.com/posts/6-common-api-errors


}