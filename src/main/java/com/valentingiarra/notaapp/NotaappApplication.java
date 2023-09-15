package com.valentingiarra.notaapp;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
@EnableEncryptableProperties
public class NotaappApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
		System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
		System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
		System.setProperty("DB_NAME", Objects.requireNonNull(dotenv.get("DB_NAME")));
		System.setProperty("JWT_SECRET", Objects.requireNonNull(dotenv.get("JWT_SECRET")));

		SpringApplication.run(NotaappApplication.class, args);

		System.out.println("---- Running ----");
	}

}
