package excepciones;

/**
 * Se lanza cuando un usuario no supera el proceso de autenticacion.
 */
public class AutenticacionFallidaException extends Exception {

    public AutenticacionFallidaException(String message) {
        super(message);
    }
}
