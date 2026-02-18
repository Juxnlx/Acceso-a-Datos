package com.example.alumnos_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    // Métodos CRUD heredados
    // Query methods personalizados
    List<Alumno> findByNombre(String nombre);
    
    //Búsqueda parcial con LIKE
    List<Alumno> findByNombreContainingIgnoreCase(String nombre);
}