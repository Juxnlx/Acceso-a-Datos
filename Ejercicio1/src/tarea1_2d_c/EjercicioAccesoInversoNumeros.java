package tarea1_2d_c;

import java.io.IOException;
import java.io.RandomAccessFile;

import java.io.IOException;
import java.io.RandomAccessFile;

public class EjercicioAccesoInversoNumeros {

    public static void main(String[] args) {
    	String archivoEntrada = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\tarea1_2d_c\\entrada.txt";
        String archivoSalida = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\tarea1_2d_c\\salida.txt";

        try {
            // Crear archivo de entrada con letra+número.
            RandomAccessFile rafEntrada = new RandomAccessFile(archivoEntrada, "rw");
            
            // Limpiar si ya existía.
            rafEntrada.setLength(0); 
            rafEntrada.writeBytes("a1\n");
            rafEntrada.writeBytes("b2\n");
            rafEntrada.writeBytes("c3\n");
            rafEntrada.writeBytes("d4\n");
            rafEntrada.writeBytes("e5\n");
            rafEntrada.close();

            // Invertir el archivo.
            invertirArchivo(archivoEntrada, archivoSalida);

            System.out.println("Archivo invertido correctamente. Revisa el archivo de salida.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Método que invierte las líneas sin usar arrays ni buffers.
    public static void invertirArchivo(String entrada, String salida) throws IOException {
        RandomAccessFile lector = new RandomAccessFile(entrada, "r");
        RandomAccessFile escritor = new RandomAccessFile(salida, "rw");

        long longitud = lector.length();
        long posicion = longitud - 1;

        StringBuilder linea = new StringBuilder();

        while (posicion >= 0) {
            lector.seek(posicion);
            char c = (char) lector.readByte();

            if (c == '\n' || c == '\r') {
                if (linea.length() > 0) {
                    // Escribimos la línea tal cual (ya está en orden correcto).
                    escritor.writeBytes(linea.reverse().toString());
                    escritor.writeByte('\n');
                    
                    // Reiniciamos.
                    linea.setLength(0); 
                }
            } else {
                linea.append(c);
            }
            posicion--;
        }

        // Última línea (la primera del archivo original).
        if (linea.length() > 0) {
            escritor.writeBytes(linea.reverse().toString());
            escritor.writeByte('\n');
        }

        lector.close();
        escritor.close();
    }
}

