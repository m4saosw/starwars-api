
package br.com.massao.api.starwars.v1.swapi;

import com.fasterxml.jackson.annotation.*;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "count",
    "next",
    "previous",
    "results"
})
public class PlanetSwapi {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("next")
    private String next;
    @JsonProperty("previous")
    private String previous;
    @JsonProperty("results")
    private List<PlanetSwapiResult> results = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("next")
    public String getNext() {
        return next;
    }

    @JsonProperty("next")
    public void setNext(String next) {
        this.next = next;
    }

    @JsonProperty("previous")
    public String getPrevious() {
        return previous;
    }

    @JsonProperty("previous")
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @JsonProperty("results")
    public List<PlanetSwapiResult> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<PlanetSwapiResult> results) {
        this.results = results;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
