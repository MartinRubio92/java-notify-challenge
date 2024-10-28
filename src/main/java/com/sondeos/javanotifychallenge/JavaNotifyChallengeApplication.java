package com.sondeos.javanotifychallenge;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JavaNotifyChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaNotifyChallengeApplication.class, args);
    }

    @Bean
    public CommandLineRunner initMethod() {
        return args -> {
            // Aquí puedes colocar el código que quieres ejecutar al iniciar la aplicación
            System.out.println("La aplicación se ha iniciado.");
        };
    }
}
