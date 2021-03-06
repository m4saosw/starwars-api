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
public class InputPersonDto {
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
}
