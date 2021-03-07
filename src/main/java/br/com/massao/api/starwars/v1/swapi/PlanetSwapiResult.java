
package br.com.massao.api.starwars.v1.swapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanetSwapiResult {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanetSwapiResult that = (PlanetSwapiResult) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
