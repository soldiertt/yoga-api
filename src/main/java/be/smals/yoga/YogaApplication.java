package be.smals.yoga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YogaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YogaApplication.class, args);
	}

}
