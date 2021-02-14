package br.com.massao.api.starwars.dto;

import br.com.massao.api.starwars.model.PersonModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @NotNull
    @NotEmpty
    private String name;

    @JsonProperty("height")
    @Min(0)
    private int height;

    @JsonProperty("mass")
    @Min(0)
    private int mass;

    @JsonProperty("birth_year")
    @NotNull
    @NotEmpty
    private String birth_year;

    @JsonProperty("gender")
    @NotNull
    @NotEmpty
    private String gender;

    @JsonProperty("homeworld")
    @NotNull
    @NotEmpty
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

    /**
     * Convert from Model to Dto
     * @param people
     * @return
     */
    public List<PersonDto> listPersonDtoFrom(List<PersonModel> people) {
        return people.stream().map(model -> new PersonDto(model)).collect(Collectors.toList());
    }
}
