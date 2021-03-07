
package br.com.massao.api.starwars.dto;

import br.com.massao.api.starwars.v1.swapi.PlanetSwapiResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Planet {

    @JsonProperty("name")
    private String name;


    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Planet(PlanetSwapiResult planet) {
        this.name = planet.getName();
    }
}
