package mx.edu.uteq.idgs12.microservicio_profesor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicioProfesorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioProfesorApplication.class, args);
	}

}
