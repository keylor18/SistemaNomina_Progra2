package excepciones;

/**
 * Se lanza cuando ocurre un problema de lectura o escritura en archivos.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
