package aajdev.io.springjpaspecdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

// TODO: SE DEBE DE MODIFICAR EL ARCHIVO DE PROPIEDADES POR UNO PROPIO
@SpringBootApplication
@PropertySource("classpath:env-aaj.properties")
public class AajSpringJpaSpecDemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(AajSpringJpaSpecDemoApplication.class, args);
  }
}
