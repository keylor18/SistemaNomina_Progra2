package utilidades;

import java.util.regex.Pattern;

/**
 * Validaciones de formato utilizadas por UI y logica.
 */
public final class ValidacionesUtil {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private ValidacionesUtil() {
    }

    /**
     * Verifica si el correo tiene una estructura basica valida.
     *
     * @param correo valor a validar
     * @return true si cumple
     */
    public static boolean esCorreoValido(String correo) {
        return correo != null && EMAIL.matcher(correo.trim()).matches();
    }

    /**
     * Verifica si un texto contiene informacion real.
     *
     * @param valor texto a evaluar
     * @return true si tiene contenido
     */
    public static boolean tieneTexto(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
}
