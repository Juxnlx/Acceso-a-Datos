package tarea1_2c;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ejercicio1_2c {

    public static void main(String[] args) {

        // Archivo de entrada con las palabras separadas
        String archivoEntrada = "C:\\Users\\juanl\\git\\Acceso-a-Datos\\Ejercicio1\\src\\ejercicios2\\palabrasSeparadas.txt";

        // Archivo de salida para las palabras ordenadas
        String archivoSalida = "C:\\Users\\juanl\\git\\Acceso-a-Datos\\Ejercicio1\\src\\ejercicio2c\\palabrasOrdenadas.txt";

        // Lista para almacenar las palabras antes de ordenarlas
        List<String> palabras = new ArrayList<>();

        // LUsamos FileReader y BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    palabras.add(linea);
                }
            }
            System.out.println("Lectura de " + palabras.size() + " palabras completada.");
        } catch (IOException e) {
            System.err.println("Error de lectura del archivo de entrada: " + e.getMessage());
            return;
        }

        // Ordenamos la lista alfabéticamente
        Collections.sort(palabras);
        System.out.println("Palabras ordenadas con éxito.");

        // Usamos FileWriter y BufferedWriter
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
            for (String palabra : palabras) {
                bw.write(palabra);
                // Equivalente a escribir un salto de línea
                bw.newLine(); 
            }
            
            // Forzamos la escritura de los datos del buffer al archivo
            bw.flush(); 

            System.out.println("Archivo de salida creado y escrito:");
            System.out.println(archivoSalida);

        } catch (IOException e) {
            System.err.println("Error de escritura en el archivo de salida: " + e.getMessage());
        }
    }
}