package aajdev.io.springjpaspecdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:")
public class AajSpringJpaSpecDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AajSpringJpaSpecDemoApplication.class, args);
	}

}
