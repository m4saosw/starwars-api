package br.com.massao.api.starwars.config;

import br.com.massao.api.starwars.security.config.AuthenticationByTokenFilter;
import br.com.massao.api.starwars.security.config.AuthenticationService;
import br.com.massao.api.starwars.security.config.TokenService;
import br.com.massao.api.starwars.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    /**
     * Authentication
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }


    /**
     * Authorization
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()

                .antMatchers(HttpMethod.POST, "/v1/people").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/v1/people/create-many").hasRole("ADMIN")

                .antMatchers(HttpMethod.PUT, "/v1/people/**").hasAnyRole("USER", "ADMIN")

                .antMatchers(HttpMethod.DELETE, "/v1/people/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/auth").permitAll()

                .antMatchers("/h2/**").permitAll()  // acesso ao console h2
                .anyRequest().authenticated()
                .and().csrf().disable() // disable CORS

                // necessario para o console web do h2 (https://jessitron.com/2020/06/15/spring-security-for-h2-console/)
                .headers().frameOptions().sameOrigin()

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Nao usar sessao
                .and().addFilterBefore(new AuthenticationByTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class);
        //@formatter:on
    }

    /**
     * Statics (js, css, img, etc)
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**","/swagger*/**");
    }
}
