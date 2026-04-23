package utilidades;

import excepciones.ValidacionException;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validaciones de formato utilizadas por UI y lógica.
 */
public final class ValidacionesUtil {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern MAYUSCULA = Pattern.compile(".*[A-Z].*");
    private static final Pattern MINUSCULA = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGITO = Pattern.compile(".*\\d.*");
    private static final Pattern ESPECIAL = Pattern.compile(".*[^A-Za-z0-9\\s].*");
    private static final Pattern USERNAME = Pattern.compile("^[a-z0-9._-]{4,24}$");
    private static final Pattern TOKEN_ALFABETICO = Pattern.compile(
            "[A-Za-z\\u00C1\\u00C9\\u00CD\\u00D3\\u00DA\\u00E1\\u00E9\\u00ED\\u00F3\\u00FA\\u00D1\\u00F1]+");
    private static final Set<String> PALABRAS_COMUNES = Set.of(
            "admin", "administrador", "administracion", "usuario", "usuarios",
            "password", "pass", "contrasena", "clave", "secreto", "seguridad",
            "default", "welcome", "login", "acceso", "sistema", "nomina",
            "planilla", "empresa", "empleado", "empleados", "colaborador",
            "colaboradores", "salario", "general", "qwerty", "asdf", "zxcv",
            "office", "correo", "correoelectronico"
    );

    private ValidacionesUtil() {
    }

    /**
     * Verifica si el correo tiene una estructura básica válida.
     *
     * @param correo valor a validar
     * @return true si cumple
     */
    public static boolean esCorreoValido(String correo) {
        return correo != null && EMAIL.matcher(correo.trim()).matches();
    }

    /**
     * Verifica si un texto contiene información real.
     *
     * @param valor texto a evaluar
     * @return true si tiene contenido
     */
    public static boolean tieneTexto(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    /**
     * Normaliza y valida el nombre de usuario de acceso.
     *
     * @param username valor ingresado
     * @return nombre de usuario normalizado en minúscula
     * @throws ValidacionException si el formato es inválido
     */
    public static String normalizarUsername(String username) throws ValidacionException {
        if (!tieneTexto(username)) {
            throw new ValidacionException("El nombre de usuario es obligatorio.");
        }
        String normalizado = username.trim().toLowerCase(Locale.ROOT);
        if (!USERNAME.matcher(normalizado).matches()) {
            throw new ValidacionException(
                    "El nombre de usuario debe tener entre 4 y 24 caracteres y solo puede incluir letras, números, punto, guion y guion bajo.");
        }
        return normalizado;
    }

    /**
     * Compara dos textos ignorando mayúsculas, tildes y espacios externos.
     *
     * @param textoA primer valor
     * @param textoB segundo valor
     * @return true si representan el mismo texto
     */
    public static boolean sonTextosEquivalentes(String textoA, String textoB) {
        if (!tieneTexto(textoA) || !tieneTexto(textoB)) {
            return false;
        }
        return normalizarTexto(textoA).equals(normalizarTexto(textoB));
    }

    /**
     * Valida la fortaleza mínima requerida para una contraseña de acceso.
     *
     * @param contrasena valor a validar
     * @throws ValidacionException si la contraseña no cumple la política
     */
    public static void validarContrasenaSegura(String contrasena) throws ValidacionException {
        if (!tieneTexto(contrasena)) {
            throw new ValidacionException("La contraseña es obligatoria.");
        }
        if (contrasena.length() < ConstantesSeguridad.LONGITUD_MINIMA_CONTRASENA) {
            throw new ValidacionException("La contraseña debe tener al menos "
                    + ConstantesSeguridad.LONGITUD_MINIMA_CONTRASENA + " caracteres.");
        }
        if (contrasena.chars().anyMatch(Character::isWhitespace)) {
            throw new ValidacionException("La contraseña no puede contener espacios.");
        }
        if (!MAYUSCULA.matcher(contrasena).matches()) {
            throw new ValidacionException("La contraseña debe incluir al menos una letra mayúscula.");
        }
        if (!MINUSCULA.matcher(contrasena).matches()) {
            throw new ValidacionException("La contraseña debe incluir al menos una letra minúscula.");
        }
        if (!DIGITO.matcher(contrasena).matches()) {
            throw new ValidacionException("La contraseña debe incluir al menos un número.");
        }
        if (!ESPECIAL.matcher(contrasena).matches()) {
            throw new ValidacionException("La contraseña debe incluir al menos un carácter especial.");
        }
        if (contienePalabraComun(contrasena)) {
            throw new ValidacionException("La contraseña no puede contener palabras comunes de diccionario.");
        }
    }

    private static boolean contienePalabraComun(String contrasena) {
        String valorSegmentado = contrasena.replaceAll(
                "(?<=[a-z\\u00E1\\u00E9\\u00ED\\u00F3\\u00FA\\u00F1])(?=[A-Z\\u00C1\\u00C9\\u00CD\\u00D3\\u00DA\\u00D1])",
                " ");
        java.util.regex.Matcher matcher = TOKEN_ALFABETICO.matcher(valorSegmentado);
        while (matcher.find()) {
            String token = normalizarTexto(matcher.group());
            if (token.length() >= 4 && PALABRAS_COMUNES.contains(token)) {
                return true;
            }
        }

        String soloLetras = normalizarTexto(contrasena).replaceAll("[^a-z]", "");
        return soloLetras.length() >= 4 && PALABRAS_COMUNES.contains(soloLetras);
    }

    private static String normalizarTexto(String valor) {
        String valorNormalizado = Normalizer.normalize(valor.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return valorNormalizado.toLowerCase(Locale.ROOT);
    }
}
