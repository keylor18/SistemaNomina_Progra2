package utilidades;

import excepciones.PersistenciaException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utilidades para preparar el almacenamiento local del sistema.
 */
public final class SistemaArchivosUtil {

    private SistemaArchivosUtil() {
    }

    /**
     * Inicializa directorios y archivos requeridos por la aplicacion.
     *
     * @throws PersistenciaException si ocurre un error al crear la estructura
     */
    public static void inicializarEstructura() throws PersistenciaException {
        try {
            Files.createDirectories(RutasSistema.DATA_DIR);
            Files.createDirectories(RutasSistema.REPORTES_DIR);
            Files.createDirectories(RutasSistema.DOCS_DIR);
            asegurarArchivo(RutasSistema.EMPLEADOS);
            asegurarArchivo(RutasSistema.NOMINAS);
            asegurarArchivo(RutasSistema.USUARIOS);
            asegurarArchivo(RutasSistema.LOGS);
        } catch (IOException ex) {
            throw new PersistenciaException("No fue posible preparar la estructura de archivos.", ex);
        }
    }

    /**
     * Asegura que el archivo exista.
     *
     * @param ruta ruta del archivo
     * @throws IOException si falla la operacion
     */
    public static void asegurarArchivo(Path ruta) throws IOException {
        if (Files.notExists(ruta)) {
            Files.createFile(ruta);
        }
    }
}
