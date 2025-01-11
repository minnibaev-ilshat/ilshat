package mail_service.minnibaev.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "mail_service.*", "common.*" })
@ComponentScan(basePackages = { "mail_service.*", "common.*" })
@EntityScan(basePackages = { "mail_service.*", "common.*" })
@SpringBootApplication
public class MailServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailServiceApplication.class);
	}
}