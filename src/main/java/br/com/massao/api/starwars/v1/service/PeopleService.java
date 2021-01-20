package br.com.massao.api.starwars.v1.service;

import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PeopleService {

    @Autowired
    private PeopleRepository repository;

    /**
     * @return
     */
    public List<PersonModel> list() {
        log.debug("list");

        return repository.findAll();
//        List<PersonModel> list = new ArrayList<>();
//        list.add(PersonModel.builder().id(1L).birth_year("XFDFD").gender("man").height(123).homeworld("terra").mass(50).name("person").build());
//        return list;
    }
}
