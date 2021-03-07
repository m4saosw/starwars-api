package br.com.massao.api.starwars.config;

import br.com.massao.api.starwars.security.model.UserModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebMvc
public class SwaggerConfig {


    /**
     * Configura a api swagger
     *
     * @return
     */
    @Bean
    public Docket api() {
        /**
         * https://cursos.alura.com.br/course/spring-boot-seguranca-cache-monitoramento/task/57429
         */
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.massao.api.starwars"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .ignoredParameterTypes(UserModel.class)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)

                // Devido a seguranca, adicionaremos um parametro chamado Authorization para apoio na autorizacao via formulario swagger
                .globalOperationParameters(
                        Arrays.asList(
                                new ParameterBuilder()
                                        .name("Authorization")
                                        .description("Header para Token JWT")
                                        .modelRef(new ModelRef("string"))
                                        .parameterType("header")
                                        .required(false)
                                        .build()));
    }


    /**
     * Realiza configuracoes adicionais para permitir exibicao da pagina swagger-ui.html
     *
     * @return
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        /**
         * Foi necessario para exibicao da pagina
         *      * https://stackoverflow.com/questions/53453006/swagger-ui-html-page-not-working-springboot
         */
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
            }
        };
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "My Star Wars API Documentation",
                "A little REST CRUD API for study purposes that consumes the API 'Star Wars API'",
                "1.0",
                "Terms of service",
                new Contact("M4sao", "www.url.com", ""),
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }
}