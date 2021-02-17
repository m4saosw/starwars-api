package br.com.massao.api.starwars.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Validator;

@EnableWebMvc
@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
