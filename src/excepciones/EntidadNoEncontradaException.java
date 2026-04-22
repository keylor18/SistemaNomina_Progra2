package excepciones;

/**
 * Se lanza cuando una entidad requerida no existe.
 */
public class EntidadNoEncontradaException extends Exception {

    public EntidadNoEncontradaException(String message) {
        super(message);
    }
}
