package tarea1_2d_b;

import java.io.IOException;
import java.io.RandomAccessFile;

public class EjercicioAccesoInverso {

    public static void main(String[] args) {
    	String archivoEntrada = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\tarea1_2d_b\\entrada.txt";
        String archivoSalida = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\tarea1_2d_b\\salida.txt";


        try {
            // Crear archivo de entrada con letras a-e.
            RandomAccessFile rafEntrada = new RandomAccessFile(archivoEntrada, "rw");
            
            // Limpiar si ya existía.
            rafEntrada.setLength(0); 
            rafEntrada.writeBytes("a\n");
            rafEntrada.writeBytes("b\n");
            rafEntrada.writeBytes("c\n");
            rafEntrada.writeBytes("d\n");
            rafEntrada.writeBytes("e\n");
            rafEntrada.close();

            // Llamar al método que escribe el archivo inverso.
            invertirArchivo(archivoEntrada, archivoSalida);

            System.out.println("Archivo invertido correctamente. Revisa el archivo de salida.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Método que lee y escribe en orden inverso SIN usar arrays ni buffers.
    public static void invertirArchivo(String entrada, String salida) throws IOException {
        RandomAccessFile lector = new RandomAccessFile(entrada, "r");
        RandomAccessFile escritor = new RandomAccessFile(salida, "rw");
        
        // Tamaño total del archivo.
        long longitud = lector.length(); 
        
        // Posición del último carácter (ajustamos por salto de línea).
        long posicion = longitud - 2;    

        while (posicion >= 0) {
        	
        	// Nos movemos hacia atrás.
            lector.seek(posicion);       
            char letra = (char) lector.readByte();

            // Saltamos los saltos de línea (\n o \r).
            if (letra != '\n' && letra != '\r') {
                escritor.writeByte(letra);
                
                // Escribimos la letra con salto de línea.
                escritor.writeByte('\n'); 
                System.out.println("Escrita letra: " + letra);
            }
            posicion--;
        }

        lector.close();
        escritor.close();
    }
}