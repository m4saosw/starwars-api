package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.converter.PersonModelConverter;
import br.com.massao.api.starwars.dto.PersonDto;
import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.service.PeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("v1/people")
// TODO - usar interfaces
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


    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PersonDto person) {
        log.info("create person={}", person);

        PersonModel personModel = new PersonModelConverter().modelFrom(person);

        PersonModel entity = peopleService.save(personModel);

        URI location = URI.create(String.format("starwars-api/v1/people/%s", entity.getId()));


//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
//                "/{id}").buildAndExpand(course.getId()).toUri();

        // nota: ResponseEntity retornando link do novo recurso no cabecalho da requisicao
        return ResponseEntity.created(location).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        log.info("deleteById id={}", id);

        try {
            peopleService.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
