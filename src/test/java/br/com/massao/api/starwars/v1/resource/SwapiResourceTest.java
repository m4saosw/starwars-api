package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.StartWarsApiApplication;
import br.com.massao.api.starwars.v1.service.SwapiPlanetsService;
import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Com o WebMvcTest nao estava funcionando a configuracao de segurança
@ContextConfiguration(classes={StartWarsApiApplication.class })
@AutoConfigureMockMvc
class SwapiResourceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SwapiPlanetsService service;

    @BeforeEach
    void setUp() {
    }


    /**
     * Massa de teste
     * @return
     */
    private List<PlanetSwapiResult> getPlanets() {
        List<PlanetSwapiResult> results = new ArrayList<>();
        results.add(new PlanetSwapiResult());
        results.get(0).setName("planet1");

        results.add(new PlanetSwapiResult());
        results.get(1).setName("planet2");
        return results;
    }

    /**
     * ListAllPlanets TEST CASES
     *
     */

    @Test
    void givenPlanetNotFoundWhenListAllPlanetsThenReturnEmptyAndStatus200() throws Exception {
        // given
        given(service.listAllPlanets()).willReturn(new ArrayList<>());

        // when / then
        mvc.perform(get("/v1/swapi/planets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenPlanetsWhenListAllPlanetsThenReturnPlanetsStatus200() throws Exception {
        // given
        List<PlanetSwapiResult> planets = getPlanets();
        given(service.listAllPlanets()).willReturn(planets);

        // when / then
        mvc.perform(get("/v1/swapi/planets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(planets.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(planets.get(1).getName()));
    }
}