package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.v1.swapi.PlanetSwapi;
import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
// TODO - cache da lista de planetas
public class SwapiPlanetsService {
    @Value("${swapi.url}")
    private String endpoint;

    @Autowired
    private RestTemplate restTemplate;


    public boolean existsPlanetByName(String name) {
        final String url = endpoint.concat("/planets/?search=").concat(name);

        log.debug("findByName name={} url={}", name, url);

        ResponseEntity<PlanetSwapi> responses = restTemplate.getForEntity(url, PlanetSwapi.class);

        // Usando o metodo exchange
        // Obtem apenas o primeiro encontrado
        return responses.hasBody() ? responses.getBody().getCount() > 0 : false;
    }


    // TODO - adicionar busca resiliente, permitindo retorno parcial de sucesso
    // TODO - adicionar informacoes no log em caso de retorno parcial
    // TODO - refatorar
    public List<PlanetSwapiResults> listAllPlanets() {
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

        log.debug("listAllPlanets results={} resultsSize={}", results, results.size());
        return results;
    }

}
