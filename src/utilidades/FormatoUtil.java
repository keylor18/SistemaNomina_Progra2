package utilidades;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Formateadores reutilizables para UI, reportes y logs.
 */
public final class FormatoUtil {

    private static final Locale LOCALE_CR = new Locale.Builder().setLanguage("es").setRegion("CR").build();
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter PERIODO = DateTimeFormatter.ofPattern("MM/yyyy");

    private FormatoUtil() {
    }

    /**
     * Formatea un monto monetario en colones.
     *
     * @param valor monto
     * @return cadena formateada
     */
    public static String formatearMoneda(double valor) {
        NumberFormat format = NumberFormat.getCurrencyInstance(LOCALE_CR);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format.format(valor);
    }

    /**
     * Interpreta montos digitados por la persona usuaria con separadores
     * decimales o de miles flexibles.
     *
     * @param valor texto a convertir
     * @return monto numerico equivalente
     * @throws NumberFormatException si no contiene un valor numerico valido
     */
    public static double parsearMonto(String valor) {
        if (valor == null) {
            throw new NumberFormatException("El monto es obligatorio.");
        }

        String normalizado = valor.trim()
                .replace(" ", "")
                .replace("\u00A0", "")
                .replace("₡", "");
        if (normalizado.isEmpty()) {
            throw new NumberFormatException("El monto es obligatorio.");
        }

        int ultimaComa = normalizado.lastIndexOf(',');
        int ultimoPunto = normalizado.lastIndexOf('.');
        int indiceDecimal = Math.max(ultimaComa, ultimoPunto);

        if (indiceDecimal >= 0) {
            String parteEntera = normalizado.substring(0, indiceDecimal).replace(",", "").replace(".", "");
            String parteDecimal = normalizado.substring(indiceDecimal + 1).replace(",", "").replace(".", "");
            normalizado = parteDecimal.isEmpty() ? parteEntera : parteEntera + "." + parteDecimal;
        } else {
            normalizado = normalizado.replace(",", "").replace(".", "");
        }

        if (normalizado.isEmpty() || "-".equals(normalizado)) {
            throw new NumberFormatException("El monto no es valido.");
        }
        return Double.parseDouble(normalizado);
    }

    /**
     * Formatea una fecha local.
     *
     * @param fecha fecha a mostrar
     * @return fecha formateada o vacia
     */
    public static String formatearFecha(LocalDate fecha) {
        return fecha == null ? "" : FECHA.format(fecha);
    }

    /**
     * Formatea un periodo mensual.
     *
     * @param periodo periodo a mostrar
     * @return periodo formateado o vacio
     */
    public static String formatearPeriodo(YearMonth periodo) {
        return periodo == null ? "" : PERIODO.format(periodo);
    }
}
