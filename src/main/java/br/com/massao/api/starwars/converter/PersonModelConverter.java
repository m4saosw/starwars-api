package br.com.massao.api.starwars.converter;


import br.com.massao.api.starwars.dto.PersonDto;
import br.com.massao.api.starwars.model.PersonModel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PersonModelConverter {

    public PersonModel modelFrom(PersonDto person) {
        PersonModel model = PersonModel.builder()
                .name(person.getName())
                .height(person.getHeight())
                .mass(person.getMass())
                .birth_year(person.getBirth_year())
                .gender(person.getGender())
                .homeworld(person.getHomeworld())
                .build();
        return model;
    }

    public List<PersonModel> listModelFrom(List<PersonDto> people) {
        return people.stream().map(person -> this.modelFrom(person)).collect(Collectors.toList());
    }
}
