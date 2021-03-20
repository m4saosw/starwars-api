package br.com.massao.api.starwars.converter;


import br.com.massao.api.starwars.dto.InputPerson;
import br.com.massao.api.starwars.model.PersonModel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PersonModelConverter {

    public PersonModel modelFrom(InputPerson person) {
        if (person == null) return null;

        return PersonModel.builder()
                .name(person.getName())
                .height(person.getHeight())
                .mass(person.getMass())
                .birthYear(person.getBirthYear())
                .gender(person.getGender())
                .homeworld(person.getHomeworld())
                .build();
    }

    public List<PersonModel> listModelFrom(List<InputPerson> people) {
        if (people == null) return Collections.emptyList();

        return people.stream().map(this::modelFrom).collect(Collectors.toList());
    }
}
