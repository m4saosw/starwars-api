package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.v1.swapi.PlanetSwapi;
import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SwapiPlanetsServiceImpl implements SwapiPlanetsService {
    @Value("${swapi.url}")
    private String endpoint;

    @Autowired
    private RestTemplate restTemplate;

    // Auto referencia para funcionamento correto do proxy do cache, quando acionado por um metodo interno
    @Resource
    private SwapiPlanetsServiceImpl self;


    /**
     * Exists a planet with exact name
     * @param name
     * @return
     */
    @Cacheable(value = "planet")
    @Override
    public boolean existsPlanetByName(String name) {
        log.debug("existsPlanetByName name={}", name);

        // TODO - conversao em set pode ser colocada em outro cache
        Set<PlanetSwapiResult> setPlanets = self.listAllPlanets().stream().filter(planet -> planet.getName().equals(name)).collect(Collectors.toSet());

        log.debug("existsPlanetByName name={} found={} elapsedTime={} ms", name, setPlanets.size());
        return ! setPlanets.isEmpty();
    }


    /**
     * List all planets
     * @return
     */
    // TODO - adicionar busca resiliente, permitindo retorno parcial de sucesso
    // TODO - adicionar informacoes no log em caso de retorno parcial
    // TODO - refatorar
    @Cacheable(value = "planets")
    @Override
    public List<PlanetSwapiResult> listAllPlanets() {
        Instant instant = Instant.now();

        String url = endpoint.concat("/planets/");
        log.debug("listPlanets url={}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);


        List<PlanetSwapiResult> results = new ArrayList<>();
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

        return results;
    }
}
