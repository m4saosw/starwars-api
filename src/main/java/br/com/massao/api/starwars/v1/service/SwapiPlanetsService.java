package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.v1.swapi.PlanetSwapi;
import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
// TODO - cache da lista de planetas
public class SwapiPlanetsService {
    @Value("${swapi.url}")
    private String endpoint;

    @Autowired
    private RestTemplate restTemplate;


    public boolean existsPlanetByName(String name) {
        log.debug("existsPlanetByName name={}", name);
        return findPlanet(name) != null;
    }

    @Cacheable(value = "planetCache", unless = "#result != null")
    private String findPlanetOld(String name) {
        Instant now = Instant.now();
        final String url = endpoint.concat("/planets/?search=").concat(name);
        log.debug("findPlanet name={} url={}", name, url);

        ResponseEntity<PlanetSwapi> responses = restTemplate.getForEntity(url, PlanetSwapi.class);
        log.debug("findPlanet name={} url={} results={} time={} ns", name, url, responses.getBody(), Instant.from(now).getNano());
        return responses.hasBody() ? responses.getBody().getResults().get(0).getName() : null;
    }

    @Cacheable(value = "planetCache", unless = "#result != null")
    private String findPlanet(String name) {
        Instant now = Instant.now();
        log.debug("findPlanet name={}", name);

        List<PlanetSwapiResults> results = listAllPlanets();


        //log.debug("findPlanet name={} results={} time={} ns", name, responses.getBody(), Instant.from(now).getNano());
        log.debug("findPlanet name={} time={} ns", name, Instant.from(now).getNano());
        //return responses.hasBody() ? responses.getBody().getResults().get(0).getName() : null;
        List<PlanetSwapiResults> list = results.stream().filter(planet -> planet.getName().equals(name)).collect(Collectors.toList());
        return list.isEmpty() ? null : list.get(0).getName();
    }


    // TODO - adicionar busca resiliente, permitindo retorno parcial de sucesso
    // TODO - adicionar informacoes no log em caso de retorno parcial
    // TODO - refatorar
    @Cacheable(value = "planetsCache")
    public List<PlanetSwapiResults> listAllPlanets() {
        Instant now = Instant.now();

        String url = endpoint.concat("/planets/");
        log.debug("listPlanets url={}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);


        List<PlanetSwapiResults> results = new ArrayList<>();
        boolean hasMorePages = true;

        // leitura em blocos por pagina
        do {
            log.debug("listAllPlanets lendo url={}", url);

            final ResponseEntity<PlanetSwapi> responses = restTemplate.exchange(url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<PlanetSwapi>() {
                    });

            final PlanetSwapi body = responses.getBody();
            if (body == null || body.getResults().isEmpty())
                break;

            results.addAll(body.getResults());

            // Prepara a leitura da proxima pagina
            if (body.getNext() != null)
                url = body.getNext().replace("http", "https");
            else
                hasMorePages = false;

        } while (hasMorePages);


        log.debug("listAllPlanets results={} resultsSize={} time={} ns", results, results.size(), Instant.from(now).getNano());
        return results;
    }

}
