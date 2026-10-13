import java.io.*;
import java.nio.charset.StandardCharsets;

public class GestorProcesos {
    public static void main(String[] args){
        //directorios y ficheros
        File carpetaRegistros = new File("logs");
        File directorioTrabajo = new File("process");
        File bat = new File("resources", "fichero.bat");

        //crear rutas fisicas
        carpetaRegistros.mkdirs();
        directorioTrabajo.mkdirs();

        // array de process builders
        String[] nombres = {"proceso1", "proceso2", "proceso3", "proceso4"};
        ProcessBuilder[] procesos = new ProcessBuilder[4];

        //proceso 1 fichero bat con comando erroneo
        procesos[0] = new ProcessBuilder("cmd", "/c", bat.getAbsolutePath());
        procesos[0].directory(directorioTrabajo);

        //proceso 2 comando con argumentos
        procesos[1] = new ProcessBuilder("cmd", "/c", "ver");

        //proceso 3 error controlado por recurso inexistente
        procesos[2] = new ProcessBuilder("cmd", "/c", "type", "holanoexisto.txt");

        //proceso 4error controlado: comando inexistente
        procesos[3] = new ProcessBuilder("holanoexisto", "--jeje");

        //ejecucion secuencial
        for (int i = 0; i < procesos.length; i++) {
            File salida = new File(carpetaRegistros, nombres[i] + "_salida.log");
            File error = new File(carpetaRegistros, nombres[i] + "_error.log");
            procesos[i].redirectOutput(salida);
            procesos[i].redirectError(error);

            try {
                Process p = procesos[i].start();
                int codigo = p.waitFor();
                System.out.println("Resumen" + nombres[i] + " " + procesos[i].command()
                        + " - Iniciado: sí - Codigo de salida: " + codigo
                        + " - Registros: " + salida.getPath() + ", " + error.getPath());
            } catch (IOException e) {
                System.out.println("Resumen " + nombres[i] + " " + procesos[i].command()
                        + " - Iniciado: NO - Codigo de salida: no disponible - Motivo: " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Resumen " + nombres[i] + " - Espera interrumpida: " + e.getMessage());
            }
        }
    }
}
