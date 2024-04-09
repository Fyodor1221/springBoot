package ru.netology.springboot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> devapp = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);

    @Container
    private static final GenericContainer<?> prodapp = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoads() {
        ResponseEntity<String> forDevEntity = restTemplate.getForEntity("http://localhost:" + devapp.getMappedPort(8080)
                + "/profile", String.class);
        System.out.println(forDevEntity.getBody());
        Assertions.assertEquals("Current profile is dev", forDevEntity.getBody());

        ResponseEntity<String> forProdEntity = restTemplate.getForEntity("http://localhost:" + prodapp.getMappedPort(8081)
                + "/profile", String.class);
        System.out.println(forProdEntity.getBody());
        Assertions.assertEquals("Current profile is production", forProdEntity.getBody());
    }

}
