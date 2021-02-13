package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.dto.PersonDto;
import br.com.massao.api.starwars.dto.PlanetDto;
import br.com.massao.api.starwars.v1.service.SwapiPlanetsService;
import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping("/planets")
    public List<PlanetDto> listAllPlanets() {
        Instant start = Instant.now();
        log.debug("listAllPlanets");

        List<PlanetDto> planets = new ArrayList<>();

        // TODO - refactor
        service.listAllPlanets().forEach(planet -> {
            planets.add(new PlanetDto(planet));
        });

        log.debug("listAllPlanets results={} resultsSize={} elapsedTime={} ms", planets, planets.size(), Duration.between(start, Instant.now()).toMillis());
        return planets;
    }
}
