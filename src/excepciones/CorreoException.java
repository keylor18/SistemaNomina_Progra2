package excepciones;

/**
 * Se lanza cuando falla el envio de correo electronico.
 */
public class CorreoException extends Exception {

    public CorreoException(String message) {
        super(message);
    }

    public CorreoException(String message, Throwable cause) {
        super(message, cause);
    }
}
