package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SwapiPlanetsService {
    /**
     * Exists a planet with exact name
     * @param name
     * @return
     */
    boolean existsPlanetByName(String name);

    /**
     * List all planets
     * @return
     */
    List<PlanetSwapiResult> listAllPlanets();
}
