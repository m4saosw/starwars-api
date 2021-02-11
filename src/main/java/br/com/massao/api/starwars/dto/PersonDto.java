package br.com.massao.api.starwars.dto;

import br.com.massao.api.starwars.model.PersonModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor // Used by Jackson
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PersonDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    @NotEmpty(message = "Name is empty")
    private String name;

    @JsonProperty("height")
    @Min(0)
    private int height;

    @JsonProperty("mass")
    @Min(0)
    private int mass;

    @JsonProperty("birth_year")
    @NotEmpty(message = "birth_year is empty")
    private String birth_year;

    @JsonProperty("gender")
    @NotEmpty(message = "gender is empty")
    private String gender;

    @JsonProperty("homeworld")
    @NotEmpty(message = "homeworld is empty")
    private String homeworld;

    @JsonProperty("filmsExhibitions")
    private int filmsExhibitions; // TODO - query

    public PersonDto(PersonModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.height = model.getHeight();
        this.mass = model.getMass();
        this.birth_year = model.getBirth_year();
        this.gender = model.getGender();
        this.homeworld = model.getHomeworld(); // TODO - query
    }
}
