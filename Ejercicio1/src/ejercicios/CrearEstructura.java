package ejercicios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CrearEstructura {
    public static void main(String[] args) {
    	
        String rutaBase = "C:/Users/jlbarrionuevo/";
        String rutaFichero = rutaBase + "eclipse-workspace/git/repository/Ejercicio1/src/ejercicios/carpetas.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Crear objeto File para la nueva carpeta
                File nuevaCarpeta = new File(rutaBase, linea);

                if (!nuevaCarpeta.exists()) { // Comprobamos si ya existe
                    boolean creada = nuevaCarpeta.mkdir(); // Crear la carpeta
                    if (creada) {
                        System.out.println("Carpeta creada: " + nuevaCarpeta.getAbsolutePath());
                    } else {
                        System.out.println("No se pudo crear la carpeta: " + nuevaCarpeta.getAbsolutePath());
                    }
                } else {
                    System.out.println("La carpeta ya existe: " + nuevaCarpeta.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
