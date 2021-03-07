package br.com.massao.api.starwars.dto;

import br.com.massao.api.starwars.model.PersonModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor // Used by Jackson
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Person {
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

    public Person(PersonModel model) {
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
    public List<Person> listPersonFrom(List<PersonModel> people) {
        return people.stream().map(model -> new Person(model)).collect(Collectors.toList());
    }


    /**
     * Convert from Model to Dto with pagination
     * @param people
     * @return
     */
    public Page<Person> listPersonFrom(Page<PersonModel> people) {
        return people.map(person -> new Person(person));
    }
}
