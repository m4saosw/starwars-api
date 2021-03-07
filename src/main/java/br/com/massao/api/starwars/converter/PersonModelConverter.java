package br.com.massao.api.starwars.converter;


import br.com.massao.api.starwars.dto.InputPerson;
import br.com.massao.api.starwars.model.PersonModel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PersonModelConverter {

    public PersonModel modelFrom(InputPerson person) {
        if (person == null) return null;

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

    public List<PersonModel> listModelFrom(List<InputPerson> people) {
        if (people == null) return null;

        return people.stream().map(person -> this.modelFrom(person)).collect(Collectors.toList());
    }
}
