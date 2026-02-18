# API de Gestión de Alumnos

Proyecto final del módulo de Acceso a Datos - API REST para gestionar alumnos.

## Descripción

Esta API permite gestionar alumnos mediante operaciones CRUD (crear, leer, actualizar y eliminar). Incluye autenticación básica, validaciones de datos y documentación con Swagger.

## Tecnologías utilizadas

- Java 23
- Spring Boot 4.0.2
- Spring Data JPA
- Spring Security
- H2 Database
- Maven
- Swagger/OpenAPI

## Cómo ejecutar el proyecto

1. Clonar el repositorio o abrir el proyecto en Eclipse
2. Ejecutar la clase `AlumnosApiApplication.java`
3. La aplicación arrancará en `http://localhost:8080`

## Acceso a la documentación

Una vez ejecutado el proyecto, puedes acceder a Swagger en:
http://localhost:8080/swagger-ui/index.html

Desde ahí puedes ver todos los endpoints y probarlos directamente.

## Credenciales de acceso

Para probar la API necesitas autenticarte:

- **Usuario**: admin
- **Contraseña**: 1234

## Base de datos

El proyecto usa H2 en memoria. Los datos se borran al reiniciar la aplicación.

Si quieres acceder a la consola H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (dejar vacío)

## Endpoints disponibles

| Método | Ruta                     | Descripción               |
|--------|--------------------------|---------------------------|
| GET    | /alumnos                 | Obtener todos los alumnos |
| POST   | /alumnos                 | Crear un nuevo alumno     |
| GET    | /alumnos/{id}            | Obtener un alumno por ID  |
| PUT    | /alumnos/{id}            | Actualizar un alumno      |
| DELETE | /alumnos/{id}            | Eliminar un alumno        |
| GET    | /alumnos/buscar?nombre=X | Buscar alumnos por nombre |
| GET    | /alumnos/buscar-avanzado?| Búsqueda avanzada (parcial) ⭐ BONUS |
            nombre=X

## Ejemplo de uso

### Crear un alumno
```json
POST http://localhost:8080/alumnos

{
  "nombre": "Juan Pérez",
  "email": "juan@correo.com"
}
```
La respuesta incluirá el ID generado automáticamente y la fecha de registro.

## Búsqueda Avanzada (Bonus)

El endpoint `/alumnos/buscar-avanzado` permite búsquedas más flexibles:

- **Búsqueda parcial**: No necesitas escribir el nombre completo
- **Case insensitive**: No distingue mayúsculas de minúsculas

### Ejemplos:
```bash
# Buscar alumnos que contengan "juan" en su nombre
GET /alumnos/buscar-avanzado?nombre=juan

# Encuentra: "Juan Pérez", "María Juan", "JUAN CARLOS", etc.
```

**Diferencia con búsqueda normal:**
- `/alumnos/buscar?nombre=Juan` → Solo encuentra "Juan" exacto
- `/alumnos/buscar-avanzado?nombre=juan` → Encuentra cualquier nombre que contenga "juan"

## Paginación y Ordenación (Bonus)

El endpoint `/alumnos` soporta paginación y ordenación para manejar grandes volúmenes de datos:

### Parámetros disponibles:

- `page`: Número de página (comienza en 0)
- `size`: Cantidad de elementos por página
- `sortBy`: Campo por el que ordenar (id, nombre, email, fechaRegistro)
- `direction`: Dirección de ordenación (asc o desc)

### Ejemplos:
```bash
# Primera página con 10 alumnos ordenados por nombre
GET /alumnos?page=0&size=10&sortBy=nombre&direction=asc

# Segunda página con 5 alumnos ordenados por fecha (más recientes primero)
GET /alumnos?page=1&size=5&sortBy=fechaRegistro&direction=desc
```

### Respuesta:

La respuesta incluye información de paginación:
```json
{
  "content": [...alumnos de esta página...],
  "totalElements": 50,
  "totalPages": 5,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

## Validaciones

- El nombre es obligatorio y no puede estar vacío
- El email es obligatorio y debe tener formato válido
- La fecha de registro se asigna automáticamente

## Estructura del proyecto

- `Alumno.java` - Entidad JPA con los datos del alumno
- `AlumnoRepository.java` - Repositorio para acceder a la base de datos
- `AlumnoController.java` - Controlador REST con los endpoints
- `SecurityConfig.java` - Configuración de seguridad
- `SwaggerConfig.java` - Configuración de la documentación
- `GlobalExceptionHandler.java` - Manejo de errores

## Funcionalidades implementadas

### Requisitos obligatorios (100%):
- ✅ CRUD completo (40%)
- ✅ Seguridad con Spring Security (20%)
- ✅ Validaciones Bean Validation (15%)
- ✅ Documentación Swagger (15%)
- ✅ Calidad del código (10%)

### Bonus opcionales:
- ✅ Búsqueda avanzada por nombre con LIKE
- ✅ Paginación y ordenación de resultados

## Autor

Juan Luis Barrionuevo  
IES Nervión - 2º DAW  
Febrero 2026