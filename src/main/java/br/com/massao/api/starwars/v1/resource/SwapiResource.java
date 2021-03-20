package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.dto.Planet;
import br.com.massao.api.starwars.exception.ApiError;
import br.com.massao.api.starwars.v1.service.SwapiPlanetsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/swapi")
public class SwapiResource {
    @Autowired
    private SwapiPlanetsService service;

    /**
     * List all planets
     *
     * @return
     */
    @ApiOperation(value = "List all planets", notes = "This operation gets all planets from swapi API. Use a planet of this list into homeworld field when inserting or updating a person resource")
    @GetMapping("/planets")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    public List<Planet> listAllPlanets() {
        Instant start = Instant.now();
        log.debug("listAllPlanets");

        List<Planet> planets = new ArrayList<>();

        service.listAllPlanets().forEach(planet ->
                planets.add(new Planet(planet))
        );

        log.debug("listAllPlanets results={} resultsSize={} elapsedTime={} ms", planets, planets.size(), Duration.between(start, Instant.now()).toMillis());
        return planets;
    }
}
