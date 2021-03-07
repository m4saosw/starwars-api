package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SwapiPlanetsService {
    boolean existsPlanetByName(String name);

    List<PlanetSwapiResults> listAllPlanets();
}
