package utilidades;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registro simple de errores en archivo de texto.
 */
public final class RegistroLogger {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private RegistroLogger() {
    }

    /**
     * Registra una excepcion en logs.txt sin interrumpir el flujo principal.
     *
     * @param contexto descripcion funcional del error
     * @param ex excepcion ocurrida
     */
    public static void registrarError(String contexto, Exception ex) {
        try {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String bloque = "[" + FORMATO.format(LocalDateTime.now()) + "] " + contexto
                    + System.lineSeparator()
                    + ex.getMessage()
                    + System.lineSeparator()
                    + sw
                    + System.lineSeparator();
            Files.writeString(RutasSistema.LOGS, bloque, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ignored) {
            // El logger no debe romper la aplicacion.
        }
    }
}
