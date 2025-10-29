package tarea1_2d_a;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class EjercicioAccesoAleatorio {

	public static void main(String[] args) {
        String archivoEntrada = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\tarea1_2d_a\\entrada.txt";
        String archivoSalida = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\tarea1_2da_a\\salida.txt";

        try {
            // Crear el archivo de entrada con una sola letra 'a'.
            RandomAccessFile rafEntrada = new RandomAccessFile(archivoEntrada, "rw");
            // Escribimos una sola letra.
            rafEntrada.writeByte('a'); 
            rafEntrada.close();

            // Abrir archivo de entrada para lectura.
            RandomAccessFile lector = new RandomAccessFile(archivoEntrada, "r");

            // Leer la letra (seek al principio).
            lector.seek(0);
            char letra = (char) lector.readByte();
            System.out.println("Letra leída: " + letra);

            // Abrir archivo de salida para escritura aleatoria.
            RandomAccessFile escritor = new RandomAccessFile(archivoSalida, "rw");
            Random random = new Random();

            // Escribir la letra 5 veces en posiciones aleatorias.
            for (int i = 0; i < 5; i++) {
            	// Posición aleatoria entre 0 y 19.
                long posicion = random.nextInt(20); 
                escritor.seek(posicion);
                escritor.writeByte(letra);
                System.out.println("Letra '" + letra + "' escrita en posición " + posicion);
            }

            // Cerrar archivos.
            lector.close();
            escritor.close();

            System.out.println("\nProceso completado con éxito. Revisa el archivo de salida.");

        } catch (IOException e) {
            System.err.println("Error durante la operación: " + e.getMessage());
        }
    }
}
