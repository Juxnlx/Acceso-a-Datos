package tarea1_2b;

import java.io.*;

public class Ejercicio2 {

	public static void main(String[] args) {

		// Ruta del archivo original con todas las palabras pegadas
		String archivoOriginal = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\ejercicios2\\palabras.txt";

		// Ruta del archivo nuevo donde pondremos una palabra por línea
		String archivoNuevo = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\ejercicios2\\palabrasSeparadas.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(archivoOriginal));
				BufferedWriter bw = new BufferedWriter(new FileWriter(archivoNuevo))) {

			String linea = br.readLine();

			if (linea != null && !linea.isEmpty()) {
				
				// Variable para ir construyendo cada palabra
				String palabra = ""; 

				for (int i = 0; i < linea.length(); i++) {
					char c = linea.charAt(i);

					if (Character.isUpperCase(c) && !palabra.equals("")) {
						// Escribimos la palabra anterior en el archivo
						bw.write(palabra);
						bw.newLine();
						
						// Reiniciamos la palabra
						palabra = ""; 
					}
					
					// Agregamos el carácter a la palabra actual
					palabra += c; 
				}

				// Escribimos la última palabra
				if (!palabra.equals("")) {
					bw.write(palabra);
					bw.newLine();
				}
			}

			bw.flush();
			System.out.println("Archivo creado con cada palabra en una línea: " + archivoNuevo);

		} catch (IOException e) {
			System.err.println("Error al procesar el archivo: " + e.getMessage());
		}
	}
}