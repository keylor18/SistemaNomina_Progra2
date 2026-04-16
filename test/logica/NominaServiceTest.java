package logica;

import datos.EmpleadoRepositorioTxt;
import datos.NominaRepositorioTxt;
import entidades.Empleado;
import entidades.Nomina;
import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertEquals;

/**
 * Pruebas del calculo de nomina.
 */
public class NominaServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void debeCalcularNominaConRentaYCreditos() throws Exception {
        EmpleadoRepositorioTxt empleadoRepositorio = new EmpleadoRepositorioTxt(temporaryFolder.newFile("empleados.txt").toPath());
        NominaRepositorioTxt nominaRepositorio = new NominaRepositorioTxt(temporaryFolder.newFile("nominas.txt").toPath());
        Empleado empleado = new Empleado("EMP-001", "101110111", "Ana Vargas", "Analista", "Finanzas",
                "ana@empresa.com", 1_500_000.00, 2, true, LocalDate.of(2024, 1, 10), true);
        empleadoRepositorio.guardar(empleado);

        NominaService service = new NominaService(nominaRepositorio, empleadoRepositorio);
        Nomina nomina = service.generarYGuardarNomina(empleado.getId(), YearMonth.of(2026, 4));

        assertEquals(82_500.00, nomina.getDeduccionSem(), 0.01);
        assertEquals(64_950.00, nomina.getDeduccionIvm(), 0.01);
        assertEquals(15_000.00, nomina.getDeduccionBancoPopular(), 0.01);
        assertEquals(59_840.00, nomina.getDeduccionRenta(), 0.01);
        assertEquals(222_290.00, nomina.getTotalDeducciones(), 0.01);
        assertEquals(1_277_710.00, nomina.getSalarioNeto(), 0.01);
        assertEquals(402_450.00, nomina.getTotalAportesPatronales(), 0.01);
    }

    @Test
    public void debeAplicarBasesMinimasCcassCuandoSalarioEsMenor() throws Exception {
        EmpleadoRepositorioTxt empleadoRepositorio = new EmpleadoRepositorioTxt(temporaryFolder.newFile("empleados-bajas.txt").toPath());
        NominaRepositorioTxt nominaRepositorio = new NominaRepositorioTxt(temporaryFolder.newFile("nominas-bajas.txt").toPath());
        Empleado empleado = new Empleado("EMP-002", "202220222", "Luis Mora", "Auxiliar", "Operaciones",
                "luis@empresa.com", 300_000.00, 0, false, LocalDate.of(2025, 2, 5), true);
        empleadoRepositorio.guardar(empleado);

        NominaService service = new NominaService(nominaRepositorio, empleadoRepositorio);
        Nomina nomina = service.generarYGuardarNomina(empleado.getId(), YearMonth.of(2026, 4));

        assertEquals(19_073.40, nomina.getDeduccionSem(), 0.01);
        assertEquals(14_054.75, nomina.getDeduccionIvm(), 0.01);
        assertEquals(36_128.15, nomina.getTotalDeducciones(), 0.01);
    }
}
