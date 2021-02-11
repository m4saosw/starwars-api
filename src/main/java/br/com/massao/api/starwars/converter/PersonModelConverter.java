package br.com.massao.api.starwars.converter;


import br.com.massao.api.starwars.dto.PersonDto;
import br.com.massao.api.starwars.model.PersonModel;
import lombok.NoArgsConstructor;

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
}
