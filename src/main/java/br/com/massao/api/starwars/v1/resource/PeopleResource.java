package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.dto.PersonDto;
import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.service.PeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        log.info("findById id={}", id);

        Optional<PersonModel> person = null;
        try {
            person = peopleService.findById(id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        // nota: ResponseEntity usando retorno tradicional
        return new ResponseEntity<>(new PersonDto(person.get()), HttpStatus.OK);
    }

}
