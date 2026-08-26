package be.ephec.padel_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class PadelBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PadelBackendApplication.class, args);
	}

}
