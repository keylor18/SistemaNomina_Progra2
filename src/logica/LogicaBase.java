package logica;

import excepciones.ValidacionException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Funcionalidad comun para las clases de negocio.
 */
public abstract class LogicaBase {

    /**
     * Lanza una excepcion de validacion si la condicion no se cumple.
     *
     * @param condicion resultado esperado
     * @param mensaje detalle del error
     * @throws ValidacionException cuando la condicion es falsa
     */
    protected void validar(boolean condicion, String mensaje) throws ValidacionException {
        if (!condicion) {
            throw new ValidacionException(mensaje);
        }
    }

    /**
     * Redondea a dos decimales.
     *
     * @param valor monto original
     * @return monto redondeado
     */
    protected double redondear(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Genera un identificador corto con prefijo.
     *
     * @param prefijo prefijo funcional
     * @return identificador legible
     */
    protected String generarId(String prefijo) {
        return prefijo + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
