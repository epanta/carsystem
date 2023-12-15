package br.com.tce.carsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CarSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarSystemApplication.class, args);
	}

}
