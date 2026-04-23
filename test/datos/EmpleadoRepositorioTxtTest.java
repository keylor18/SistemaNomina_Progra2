package datos;

import entidades.Empleado;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import utilidades.TextoPlanoUtil;

/**
 * Pruebas de persistencia de empleados.
 */
public class EmpleadoRepositorioTxtTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void debeGuardarYLeerEmpleado() throws Exception {
        File archivo = temporaryFolder.newFile("empleados.txt");
        EmpleadoRepositorioTxt repositorio = new EmpleadoRepositorioTxt(archivo.toPath());

        Empleado empleado = crearEmpleado("EMP-001", "101110111", "Ana Vargas", "Analista",
                "Finanzas", "ana@empresa.com", 850000.00);
        repositorio.guardar(empleado);

        List<Empleado> empleados = repositorio.obtenerTodos();
        assertEquals(1, empleados.size());
        assertEquals("Ana Vargas", empleados.get(0).getNombreCompleto());
        assertEquals(15, empleados.get(0).getDiasVacacionesAsignados());
        assertEquals(0, empleados.get(0).getDiasVacacionesTomados());
        assertTrue(repositorio.buscarPorId("EMP-001").isPresent());
    }

    @Test
    public void debeInterpretarRegistrosAntiguosSinVacaciones() throws Exception {
        File archivo = temporaryFolder.newFile("empleados-antiguos.txt");
        Files.writeString(archivo.toPath(),
                String.join("|",
                        TextoPlanoUtil.codificarCampo("EMP-002"),
                        TextoPlanoUtil.codificarCampo("202220222"),
                        TextoPlanoUtil.codificarCampo("Luis Mora"),
                        TextoPlanoUtil.codificarCampo("Auxiliar"),
                        TextoPlanoUtil.codificarCampo("Operaciones"),
                        TextoPlanoUtil.codificarCampo("luis@empresa.com"),
                        "300000.00",
                        "0",
                        "false",
                        "2025-02-05",
                        "true"));
        EmpleadoRepositorioTxt repositorio = new EmpleadoRepositorioTxt(archivo.toPath());

        Empleado empleado = repositorio.obtenerTodos().get(0);
        assertEquals(15, empleado.getDiasVacacionesAsignados());
        assertEquals(0, empleado.getDiasVacacionesTomados());
        assertEquals(15, empleado.getDiasVacacionesDisponibles());
    }

    private Empleado crearEmpleado(String id, String cedula, String nombre, String puesto,
            String departamento, String correo, double salario)
            throws PersistenciaException, ValidacionException {
        return new Empleado(id, cedula, nombre, puesto, departamento, correo, salario, 1,
                false, LocalDate.of(2024, 1, 10), true);
    }
}
