package tarea1_1a;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CrearEstructura {
    public static void main(String[] args) {
    	
        String rutaBase = "C:\\Users\\jlbarrionuevo\\";
        String archivoEstructura = rutaBase + "eclipse-workspace\\Ejercicio1\\src\\ejercicios\\carpetas.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(archivoEstructura))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Path rutaCompleta = Paths.get(rutaBase, linea);

                    if (!Files.exists(rutaCompleta)) {
                        Files.createDirectories(rutaCompleta);
                        System.out.println("Carpeta creada: " + rutaCompleta);
                    } else {
                        System.out.println("Ya existe: " + rutaCompleta);
                    }
                }
            }
            System.out.println("\n✅ Estructura de carpetas creada/comprobada en " + rutaBase);
        } catch (IOException e) {
            System.err.println("Error al crear la estructura: " + e.getMessage());
        }
    }
}
