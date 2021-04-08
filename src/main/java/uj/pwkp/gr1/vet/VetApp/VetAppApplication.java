package uj.pwkp.gr1.vet.VetApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VetAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetAppApplication.class, args);
	}

}
