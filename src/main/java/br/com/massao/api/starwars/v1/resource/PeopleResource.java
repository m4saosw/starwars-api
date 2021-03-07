package br.com.massao.api.starwars.v1.resource;

import br.com.massao.api.starwars.converter.PersonModelConverter;
import br.com.massao.api.starwars.dto.InputPersonDto;
import br.com.massao.api.starwars.dto.PersonDto;
import br.com.massao.api.starwars.exception.ApiError;
import br.com.massao.api.starwars.exception.NotFoundException;
import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.service.PeopleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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

    @ApiOperation(value = "List all people")
    @GetMapping
    @ApiResponses(value={
            @ApiResponse(code=500, message="Internal Server Error")
    })
    public Page<PersonDto> list(@PageableDefault(size = 5, sort = "id") Pageable pageRequest ) {
        log.info("list with pageable={}", pageRequest);

        return new PersonDto().listPersonDtoFrom(peopleService.list(pageRequest));
    }

    @ApiOperation(value = "Find a person by id")
    @GetMapping("/{id}")
    @ApiResponses(value={
            @ApiResponse(code=404, message="Not Found"),
            @ApiResponse(code=400, message="Bad Request", response = ApiError.class),
            @ApiResponse(code=500, message="Internal Server Error", response = ApiError.class)
    })
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

    @ApiOperation(value = "Create a person")
    @PostMapping
    @ApiResponses(value={
            @ApiResponse(code=201, message="Created"),
            @ApiResponse(code=400, message="Bad Request", response = ApiError.class),
            @ApiResponse(code=500, message="Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<?> create(@Valid @RequestBody InputPersonDto person, UriComponentsBuilder uriBuilder) {
        log.info("create person={}", person);

        PersonModel personModel = converter.modelFrom(person);
        PersonModel entity = peopleService.save(personModel);

        // nota: ResponseEntity retornando link do novo recurso no cabecalho da requisicao
        // Alguns dizem que devolver body na requisicao nao e adequado pois aumenta o trafego de dados
        URI uri = uriBuilder.path("/people/{id}").buildAndExpand(entity.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }


    @ApiOperation(value = "Delete a person")
    @DeleteMapping("/{id}")
    @ApiResponses(value={
            @ApiResponse(code=204, message="Successfully Deleted"),
            @ApiResponse(code=403, message="Access Denied. Please authenticate first to get a valid token. Only ADMIN role is permitted."),
            @ApiResponse(code=404, message="Not Found"),
            @ApiResponse(code=500, message="Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        log.info("deleteById id={}", id);

        try {
            peopleService.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Update a person")
    @PutMapping("/{id}")
    @ApiResponses(value={
            @ApiResponse(code=200, message="Successfully Updated"),
            @ApiResponse(code=400, message="Bad Request", response = ApiError.class),
            @ApiResponse(code=403, message="Access Denied. Please authenticate first to get a valid token. Only USER or ADMIN roles are permitted."),
            @ApiResponse(code=404, message="Not Found"),
            @ApiResponse(code=500, message="Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody InputPersonDto person) {
        log.info("modify id={} person={}", id, person);

        try {
            PersonModel model = converter.modelFrom(person);
            Optional<PersonModel> modified = peopleService.update(id, model);

            return ResponseEntity.ok().body(new PersonDto(modified.get()));

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Create many people")
    @PostMapping("/create-many")
    @ApiResponses(value={
            @ApiResponse(code=201, message="Created"),
            @ApiResponse(code=400, message="Bad Request", response = ApiError.class),
            @ApiResponse(code=403, message="Access Denied. Please authenticate first to get a valid token. Only ADMIN role is permitted."),
            @ApiResponse(code=500, message="Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<?> createMany(@Valid @RequestBody List<InputPersonDto> people, UriComponentsBuilder uriBuilder) {
        log.info("createMany people={}", people);

        List<PersonModel> listModel = converter.listModelFrom(people);

        List<PersonModel> entities = peopleService.saveMany(listModel);

        List<PersonDto> result = new PersonDto().listPersonDtoFrom(entities);

        // Points to the list uri
        URI uri = uriBuilder.path("/people").build().toUri();
        return ResponseEntity.created(uri).body(result);
    }
}
