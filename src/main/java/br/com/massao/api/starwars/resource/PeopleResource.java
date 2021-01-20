package br.com.massao.api.starwars.resource;

import br.com.massao.api.starwars.dto.PersonDto;
import br.com.massao.api.starwars.service.PeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/people")
public class PeopleResource {
    @Autowired
    private PeopleService peopleService;

    @GetMapping
    public List<PersonDto> list() {
        log.info("list");

        List<PersonDto> people = new ArrayList<>();

        // TODO - refactor
        peopleService.list().forEach(person -> {
            people.add(new PersonDto(person));
        });

        return people;

    }



}
