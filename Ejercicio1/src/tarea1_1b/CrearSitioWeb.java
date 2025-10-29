package tarea1_1b;

import java.io.*;

// Programa que recorre la estructura de carpetas indicada en carpetas.txt 
// y crea un fichero index.html en cada carpeta.
public class CrearSitioWeb {

	public static void main(String[] args) {

		// Ruta donde se encuentra el archivo carpetas.txt dentro de Eclipse
		String archivoEstructura = "C:\\Users\\jlbarrionuevo\\git\\repository\\Ejercicio1\\src\\ejercicios\\carpetas.txt";


		// Nombre del autor que aparecerá en cada HTML
		String autor = "Juan Luis Barrionuevo"; 

		// Capturamos el código que puede lanzar excepciones
		try (BufferedReader br = new BufferedReader(new FileReader(archivoEstructura))) {

			String linea;

			// Leemos el archivo línea a línea
			while ((linea = br.readLine()) != null) {

				// Saltamos líneas vacías
				if (linea.trim().isEmpty())
					continue;

				// Creamos la ruta completa de la carpeta donde se creará el HTML
				File carpeta = new File("C:\\Users\\jlbarrionuevo\\" + linea);

				// Comprobamos que la carpeta existe y es realmente un directorio
				if (carpeta.exists() && carpeta.isDirectory()) {

					// Creamos el archivo index.html dentro de la carpeta
					File html = new File(carpeta, "index.html");

					try (BufferedWriter bw = new BufferedWriter(new FileWriter(html))) {

						// Escribimos la estructura básica del HTML
						bw.write("<html>");
						bw.newLine();
						bw.write("   <head>");
						bw.newLine();
						bw.write("      <title>" + carpeta.getName() + "</title>");
						bw.newLine();
						bw.write("   </head>");
						bw.newLine();
						bw.write("   <body>");
						bw.newLine();
						bw.write("      <h1>" + carpeta.getAbsolutePath() + "</h1>");
						bw.newLine();
						bw.write("      <h3>Autor: " + autor + "</h3>");
						bw.newLine();
						bw.write("   </body>");
						bw.newLine();
						bw.write("</html>");

						// Vaciamos el buffer para asegurarnos de que todo se escribe
						bw.flush();
					}

					// Mensaje por consola para confirmar creación del archivo
					System.out.println("index.html creado en: " + carpeta.getAbsolutePath());
				}
			}

			System.out.println("\nSitio web generado correctamente en todas las carpetas.");

		} catch (IOException e) {
			System.err.println("Error al generar el sitio web: " + e.getMessage());
		}
	}
}
