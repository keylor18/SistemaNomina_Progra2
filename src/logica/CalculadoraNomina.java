package logica;

import entidades.Empleado;
import entidades.Nomina;
import excepciones.ValidacionException;
import java.time.YearMonth;

/**
 * Contrato para calculo de nomina.
 */
public interface CalculadoraNomina {

    Nomina calcularNomina(Empleado empleado, YearMonth periodo) throws ValidacionException;
}
