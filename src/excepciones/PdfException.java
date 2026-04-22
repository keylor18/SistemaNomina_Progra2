package excepciones;

/**
 * Se lanza cuando falla la generacion de un documento PDF.
 */
public class PdfException extends Exception {

    public PdfException(String message) {
        super(message);
    }

    public PdfException(String message, Throwable cause) {
        super(message, cause);
    }
}
