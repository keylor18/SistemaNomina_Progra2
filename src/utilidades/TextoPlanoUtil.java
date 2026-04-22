package utilidades;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Facilita la serializacion segura de cadenas en archivos de texto.
 */
public final class TextoPlanoUtil {

    private TextoPlanoUtil() {
    }

    /**
     * Codifica un campo a Base64.
     *
     * @param valor valor plano
     * @return texto codificado
     */
    public static String codificarCampo(String valor) {
        String seguro = valor == null ? "" : valor;
        return Base64.getUrlEncoder().encodeToString(seguro.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodifica un campo Base64.
     *
     * @param valor valor codificado
     * @return texto plano
     */
    public static String decodificarCampo(String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }
        return new String(Base64.getUrlDecoder().decode(valor), StandardCharsets.UTF_8);
    }
}
