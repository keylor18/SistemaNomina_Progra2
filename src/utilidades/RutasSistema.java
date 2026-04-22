package utilidades;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Define las rutas fisicas utilizadas por la aplicacion.
 */
public final class RutasSistema {

    public static final Path BASE = Paths.get(System.getProperty("user.dir"));
    public static final Path DATA_DIR = BASE.resolve("data");
    public static final Path REPORTES_DIR = BASE.resolve("reportes");
    public static final Path DOCS_DIR = BASE.resolve("docs");
    public static final Path EMPLEADOS = DATA_DIR.resolve("empleados.txt");
    public static final Path NOMINAS = DATA_DIR.resolve("nominas.txt");
    public static final Path USUARIOS = DATA_DIR.resolve("usuarios.txt");
    public static final Path LOGS = DATA_DIR.resolve("logs.txt");

    private RutasSistema() {
    }
}
