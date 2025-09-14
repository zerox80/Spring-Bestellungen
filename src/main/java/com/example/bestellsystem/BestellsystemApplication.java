package com.example.bestellsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BestellsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestellsystemApplication.class, args);
    }

}
