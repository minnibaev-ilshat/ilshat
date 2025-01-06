package rest_service.com.minnibaev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = { "rest_service.*" , "common.*" })
@ComponentScan(basePackages = { "rest_service.*" , "common.*" })
@EntityScan(basePackages = {"rest_service.*" , "common.*"})
@SpringBootApplication
public class RestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class);
    }
}