package br.com.massao.api.starwars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableCaching
@SpringBootApplication
//@EnableSpringDataWebSupport // receber os parâmetros de ordenação e paginação diretamente nos métodos do controller
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
