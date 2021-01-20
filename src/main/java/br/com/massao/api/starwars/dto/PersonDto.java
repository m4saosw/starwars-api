package br.com.massao.api.starwars.dto;

import br.com.massao.api.starwars.model.PersonModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor // Used by Jackson
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PersonDto {
    private Long id;

    private String name;

    private int height;

    private int mass;

    private String birth_year;

    private String gender;

    private int homeworld;

    private int filmsExhibitions; // TODO - query

    public PersonDto(PersonModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.height = model.getHeight();
        this.mass = model.getMass();
        this.birth_year = model.getBirth_year();
        this.gender = model.getGender();
        // this.homeworld = model.getHomeworld(); // TODO - query
    }
}
