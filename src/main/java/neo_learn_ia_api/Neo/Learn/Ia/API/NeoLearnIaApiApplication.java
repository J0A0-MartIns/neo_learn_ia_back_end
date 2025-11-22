package neo_learn_ia_api.Neo.Learn.Ia.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NeoLearnIaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeoLearnIaApiApplication.class, args);
	}

}
