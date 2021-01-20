package br.com.massao.api.starwars;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *  Sanity check test
 *  Will fail if the application context cannot start
 */
//@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringContextTest {

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}
