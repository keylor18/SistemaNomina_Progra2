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
