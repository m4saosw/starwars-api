package br.com.massao.api.starwars.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor // Used by Jackson
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class InputPerson {
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
    private String birthYear;

    @JsonProperty("gender")
    @NotNull
    @NotEmpty
    private String gender;

    @JsonProperty("homeworld")
    @NotNull
    @NotEmpty
    private String homeworld;
}
