package excepciones;

/**
 * Se lanza cuando una entidad no cumple las reglas del negocio.
 */
public class ValidacionException extends Exception {

    public ValidacionException(String message) {
        super(message);
    }
}
