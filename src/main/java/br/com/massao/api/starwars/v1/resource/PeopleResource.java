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
import org.springframework.web.util.UriComponentsBuilder;

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
    private PersonModelConverter converter = new PersonModelConverter();

    @GetMapping
    public List<PersonDto> list() {
        log.info("list");

        return new PersonDto().listPersonDtoFrom(peopleService.list());
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
    public ResponseEntity<?> create(@Valid @RequestBody PersonDto person, UriComponentsBuilder uriBuilder) {
        log.info("create person={}", person);

        PersonModel personModel = converter.modelFrom(person);
        PersonModel entity = peopleService.save(personModel);

        // nota: ResponseEntity retornando link do novo recurso no cabecalho da requisicao
        // Alguns dizem que devolver body na requisicao nao e adequado pois aumenta o trafego de dados
        URI uri = uriBuilder.path("/people/{id}").buildAndExpand(entity.getId()).toUri();

        return ResponseEntity.created(uri).build();
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


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody PersonDto person) {
        log.info("modify id={} person={}", id, person);

        try {
            PersonModel model = converter.modelFrom(person);
            Optional<PersonModel> modified = peopleService.update(id, model);

            return ResponseEntity.ok().body(new PersonDto(modified.get()));

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/create-many")
    public ResponseEntity<?> createMany(@Valid @RequestBody List<PersonDto> people, UriComponentsBuilder uriBuilder) {
        log.info("createMany people={}", people);

        List<PersonModel> listModel = converter.listModelFrom(people);

        List<PersonModel> entities = peopleService.saveMany(listModel);

        List<PersonDto> result = new PersonDto().listPersonDtoFrom(entities);

        // Points to the list uri
        URI uri = uriBuilder.path("/people").build().toUri();
        return ResponseEntity.created(uri).body(result);
    }
}
