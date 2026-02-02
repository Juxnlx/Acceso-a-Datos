package com.example.alumnos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "API de Gestión de Alumnos",
        version = "1.0",
        description = "API REST para gestionar alumnos con operaciones CRUD completas, " +
                      "validaciones y autenticación básica",
        contact = @Contact(
            name = "Juan Luis Barrionuevo",
            email = "juanluis.barrionuevo@iesnerion.es"
        )
    )
)
public class AlumnosApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlumnosApiApplication.class, args);
    }

}
