package utilidades;

import java.util.List;

/**
 * Centraliza parametros oficiales y operativos usados por el sistema.
 */
public final class ConstantesNomina {

    /**
     * Fecha normativa de referencia del sistema.
     */
    public static final String FECHA_REFERENCIA = "2026-01-01";
    public static final double BASE_MINIMA_SEM_2026 = 346_789.00;
    public static final double BASE_MINIMA_IVM_2026 = 324_590.00;
    public static final double SEM_TRABAJADOR = 0.0550;
    public static final double SEM_PATRONO = 0.0925;
    public static final double IVM_TRABAJADOR = 0.0433;
    public static final double IVM_PATRONO = 0.0558;
    public static final double BANCO_POPULAR_TRABAJADOR = 0.0100;
    public static final double BANCO_POPULAR_PATRONAL = 0.0025;
    public static final double BANCO_POPULAR_LPT_PATRONAL = 0.0025;
    public static final double ASIGNACIONES_FAMILIARES = 0.0500;
    public static final double IMAS = 0.0050;
    public static final double INA = 0.0150;
    public static final double FCL = 0.0150;
    public static final double ROP = 0.0200;
    public static final double INS = 0.0100;
    public static final int DIAS_VACACIONES_ANUALES = 15;
    public static final double HORAS_ORDINARIAS_MENSUALES = 240.0;
    public static final double FACTOR_HORA_EXTRA = 1.50;
    public static final int MAX_INTENTOS_LOGIN = 3;
    public static final double CREDITO_HIJO_MENSUAL = 1_710.00;
    public static final double CREDITO_CONYUGE_MENSUAL = 2_590.00;
    public static final List<TramoRenta> TRAMOS_RENTA_MENSUAL_2026 = List.of(
            new TramoRenta(918_000.00, 0.00),
            new TramoRenta(1_347_000.00, 0.10),
            new TramoRenta(2_364_000.00, 0.15),
            new TramoRenta(4_727_000.00, 0.20),
            new TramoRenta(Double.POSITIVE_INFINITY, 0.25)
    );
    public static final String FUENTE_CCSS = "https://aissfa.ccss.sa.cr/noticias/noticia?v=822124515110";
    public static final String FUENTE_HACIENDA = "https://www.hacienda.go.cr/docs/TramosRenta2026.pdf";
    public static final String FUENTE_PATRONOS = "https://aissfa.ccss.sa.cr/patronos";
    private ConstantesNomina() {
    }
}
