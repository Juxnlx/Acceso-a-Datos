package com.example.alumnos_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alumnos")
@Tag(name = "Alumnos", description = "API para la gestión de alumnos")
public class AlumnoController {
    
    @Autowired
    private AlumnoRepository repo;
    
 // 1. LISTAR TODOS LOS ALUMNOS (GET) - CON PAGINACIÓN
    @Operation(
        summary = "Listar todos los alumnos con paginación",
        description = "Obtiene una lista paginada de todos los alumnos registrados en el sistema. " +
                      "Soporta ordenación por cualquier campo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de alumnos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    })
    @GetMapping
    public Page<Alumno> listar(
        @Parameter(description = "Número de página (0-indexed)", example = "0")
        @RequestParam(defaultValue = "0") int page,
        
        @Parameter(description = "Tamaño de página", example = "10")
        @RequestParam(defaultValue = "10") int size,
        
        @Parameter(description = "Campo por el que ordenar", example = "nombre")
        @RequestParam(defaultValue = "id") String sortBy,
        
        @Parameter(description = "Dirección de ordenación (asc o desc)", example = "asc")
        @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return repo.findAll(pageable);
    }
    
    // 2. CREAR ALUMNO (POST)
    @Operation(
        summary = "Crear un nuevo alumno",
        description = "Registra un nuevo alumno en el sistema. El email debe ser único y válido. La fecha de registro se asigna automáticamente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alumno creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos - Revisar validaciones"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    })
    @PostMapping
    public Alumno crear(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del alumno a crear (nombre y email requeridos)",
            required = true
        )
        @Valid @RequestBody Alumno alumno
    ) {
        return repo.save(alumno);
    }
    
    // 3. ACTUALIZAR ALUMNO (PUT)
    @Operation(
        summary = "Actualizar un alumno existente",
        description = "Actualiza los datos de un alumno identificado por su ID. La fecha de registro original se preserva."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alumno actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos - Revisar validaciones"),
        @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizar(
        @Parameter(description = "ID del alumno a actualizar", required = true, example = "1")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevos datos del alumno (nombre y email)",
            required = true
        )
        @Valid @RequestBody Alumno alumno
    ) {
        Optional<Alumno> alumnoExistente = repo.findById(id);
        if (alumnoExistente.isPresent()) {
            Alumno a = alumnoExistente.get();
            a.setNombre(alumno.getNombre());
            a.setEmail(alumno.getEmail());
            return ResponseEntity.ok(repo.save(a));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 4. ELIMINAR ALUMNO (DELETE)
    @Operation(
        summary = "Eliminar un alumno",
        description = "Elimina permanentemente un alumno del sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Alumno eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID del alumno a eliminar", required = true, example = "1")
        @PathVariable Long id
    ) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 5. BUSCAR ALUMNO POR ID (GET)
    @Operation(
        summary = "Buscar alumno por ID",
        description = "Obtiene los datos de un alumno específico mediante su identificador"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alumno encontrado"),
        @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> buscarPorId(
        @Parameter(description = "ID del alumno a buscar", required = true, example = "1")
        @PathVariable Long id
    ) {
        Optional<Alumno> alumno = repo.findById(id);
        if (alumno.isPresent()) {
            return ResponseEntity.ok(alumno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 6. BUSCAR POR NOMBRE (GET)
    @Operation(
        summary = "Buscar alumnos por nombre",
        description = "Busca todos los alumnos que coincidan exactamente con el nombre proporcionado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    })
    @GetMapping("/buscar")
    public List<Alumno> buscarPorNombre(
        @Parameter(description = "Nombre del alumno a buscar", required = true, example = "Juan Luis")
        @RequestParam String nombre
    ) {
        return repo.findByNombre(nombre);
    }
    
    // 7. BÚSQUEDA AVANZADA POR NOMBRE (GET) - BONUS
    @Operation(
        summary = "Búsqueda avanzada por nombre",
        description = "Busca alumnos cuyo nombre contenga el texto proporcionado (búsqueda parcial, sin distinguir mayúsculas)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
    })
    @GetMapping("/buscar-avanzado")
    public List<Alumno> buscarPorNombreAvanzado(
        @Parameter(description = "Texto a buscar en el nombre del alumno", required = true, example = "Juan")
        @RequestParam String nombre
    ) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}