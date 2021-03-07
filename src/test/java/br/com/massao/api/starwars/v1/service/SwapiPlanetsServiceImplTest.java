package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.v1.swapi.PlanetSwapi;
import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResults;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class SwapiPlanetsServiceImplTest {
    @Autowired
    private SwapiPlanetsService swapiPlanetsService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private SwapiPlanetsServiceImpl self;


    @BeforeEach
    void setUp() {
    }


    /**
     * Massa de teste
     * @return
     */
    private List<PlanetSwapiResults> getPlanets() {
        List<PlanetSwapiResults> results = new ArrayList<>();
        results.add(new PlanetSwapiResults());
        results.get(0).setName("planet1");

        results.add(new PlanetSwapiResults());
        results.get(1).setName("planet2");
        return results;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenValidPlanetNameWhenExistsPlanetByNameThenReturnTrue() {
        // given
        List<PlanetSwapiResults> results = getPlanets();
        Mockito.when(self.listAllPlanets()).thenReturn(results);

        // when
        boolean existsPlanet = swapiPlanetsService.existsPlanetByName(results.get(0).getName());

        // then
        assertThat(existsPlanet).isTrue();
    }




    @Test
    void givenNotFoundPlanetNameWhenExistsPlanetByNameThenReturnFalse() {
        // given
        Mockito.when(self.listAllPlanets()).thenReturn(getPlanets());

        // when
        boolean existsPlanet = swapiPlanetsService.existsPlanetByName("planetNotFound");

        // then
        assertThat(existsPlanet).isFalse();
    }


    @Test
    void givenPlanetsOnSinglePageWhenlistAllPlanetsThenReturnPlanets() {
        // GIVEN
        List<PlanetSwapiResults> planets = getPlanets(); // planets

        // entity body
        PlanetSwapi planetSwapi = new PlanetSwapi();
        planetSwapi.setResults(planets);

        //
        ResponseEntity<PlanetSwapi> entity = new ResponseEntity<>(planetSwapi, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<HttpEntity<PlanetSwapi>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<PlanetSwapi>>any())
        ).thenReturn(entity);
        
        
        // WHEN
        List<PlanetSwapiResults> results = swapiPlanetsService.listAllPlanets();

        // THEN
        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.contains(planets.get(0))).isTrue();
        assertThat(results.contains(planets.get(1))).isTrue();
    }


    @Test
    void givenPlanetsNotFoundWhenlistAllPlanetsThenReturnEmpty() {
        // GIVEN
        List<PlanetSwapiResults> planets = new ArrayList<>(); // no results

        // entity body
        PlanetSwapi planetSwapi = new PlanetSwapi();
        planetSwapi.setResults(planets);

        //
        ResponseEntity<PlanetSwapi> entity = new ResponseEntity<>(planetSwapi, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<HttpEntity<PlanetSwapi>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<PlanetSwapi>>any())
        ).thenReturn(entity);


        // WHEN
        List<PlanetSwapiResults> results = swapiPlanetsService.listAllPlanets();

        // THEN
        assertThat(results).isEmpty();
    }



    /**
     * TestConfiguration guarantee this bean is only for test scope
     */
    @TestConfiguration
    static class SwapiPlanetsServiceImplTestContextConfiguration {
        @Bean
        public SwapiPlanetsService swapiPlanetsService() {
            return new SwapiPlanetsServiceImpl();
        }

    }

    //  https://www.sisense.com/blog/rest-api-testing-strategy-what-exactly-should-you-test/
}