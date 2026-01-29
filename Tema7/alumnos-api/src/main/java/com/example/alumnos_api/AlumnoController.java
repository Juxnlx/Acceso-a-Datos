package com.example.alumnos_api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/alumnos")
public class AlumnoController {
 
 @Autowired
 private AlumnoRepository repo;
 
 @GetMapping
 public List<Alumno> listar() {
 return repo.findAll();
 }
 
 @PostMapping
 public Alumno crear(
 @RequestBody Alumno alumno) {
 return repo.save(alumno);
 }
}
