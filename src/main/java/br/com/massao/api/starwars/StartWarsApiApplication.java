package br.com.massao.api.starwars;

import br.com.massao.api.starwars.model.PersonModel;
import br.com.massao.api.starwars.v1.repository.PeopleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication
public class StartWarsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartWarsApiApplication.class, args);
    }

//
//    @Bean
//    public CommandLineRunner init(PeopleRepository repository) {
//        return (args) -> {
//            PersonModel person = PersonModel.builder().birth_year("XFDSR").gender("man").height(123).homeworld("terra").mass(50).name("person1").build();
//            repository.save(person);
//        };
//    }

}
