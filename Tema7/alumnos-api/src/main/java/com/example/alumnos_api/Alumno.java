package com.example.alumnos_api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Modelo de datos de un alumno")
public class Alumno {
    
    @Id
    @GeneratedValue
    @Schema(description = "Identificador único del alumno", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @NotNull(message = "El nombre es requerido")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre completo del alumno", example = "Juan Luis Barrionuevo", required = true)
    private String nombre;
    
    @NotNull(message = "El email es requerido")
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    @Schema(description = "Correo electrónico del alumno", example = "juanluis.barrionuevo@iesnervion.es", required = true)
    private String email;
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}