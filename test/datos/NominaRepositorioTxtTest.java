package datos;

import entidades.Nomina;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertEquals;
import utilidades.TextoPlanoUtil;

/**
 * Pruebas de persistencia de nominas.
 */
public class NominaRepositorioTxtTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void debeGuardarYLeerNominaConHorasExtra() throws Exception {
        File archivo = temporaryFolder.newFile("nominas.txt");
        NominaRepositorioTxt repositorio = new NominaRepositorioTxt(archivo.toPath());

        Nomina nomina = new Nomina();
        nomina.setId("NOM-001");
        nomina.setEmpleadoId("EMP-001");
        nomina.setNombreEmpleado("Ana Vargas");
        nomina.setPeriodo(YearMonth.of(2026, 4));
        nomina.setFechaGeneracion(LocalDate.of(2026, 4, 16));
        nomina.setSalarioBaseOrdinario(960000);
        nomina.setHorasExtra(10);
        nomina.setMontoHorasExtra(60000);
        nomina.setSalarioBruto(1020000);
        nomina.setDeduccionSem(56100);
        nomina.setDeduccionIvm(44166);
        nomina.setDeduccionBancoPopular(10200);
        nomina.setDeduccionRenta(10200);
        nomina.setTotalDeducciones(120666);
        nomina.setAporteSemPatrono(94350);
        nomina.setAporteIvmPatrono(56916);
        nomina.setAporteAsignacionesFamiliares(51000);
        nomina.setAporteImas(5100);
        nomina.setAporteIna(15300);
        nomina.setAporteBancoPopularPatronal(2550);
        nomina.setAporteBancoPopularLpt(2550);
        nomina.setAporteFcl(15300);
        nomina.setAporteRop(20400);
        nomina.setAporteIns(10200);
        nomina.setTotalAportesPatronales(273666);
        nomina.setSalarioNeto(899334);
        nomina.setRutaPdf("reporte.pdf");
        repositorio.guardar(nomina);

        List<Nomina> nominas = repositorio.obtenerTodos();
        assertEquals(1, nominas.size());
        assertEquals(960000, nominas.get(0).getSalarioBaseOrdinario(), 0.01);
        assertEquals(10, nominas.get(0).getHorasExtra(), 0.01);
        assertEquals(60000, nominas.get(0).getMontoHorasExtra(), 0.01);
    }

    @Test
    public void debeInterpretarRegistrosAntiguosSinHorasExtra() throws Exception {
        File archivo = temporaryFolder.newFile("nominas-antiguas.txt");
        Files.writeString(archivo.toPath(),
                String.join("|",
                        TextoPlanoUtil.codificarCampo("NOM-002"),
                        TextoPlanoUtil.codificarCampo("EMP-002"),
                        TextoPlanoUtil.codificarCampo("Luis Mora"),
                        "2026-04",
                        "2026-04-16",
                        "300000.00",
                        "19073.40",
                        "14054.75",
                        "3000.00",
                        "0.00",
                        "36128.15",
                        "32078.98",
                        "18114.12",
                        "15000.00",
                        "1500.00",
                        "4500.00",
                        "750.00",
                        "750.00",
                        "4500.00",
                        "6000.00",
                        "3000.00",
                        "86193.10",
                        "263871.85",
                        ""));
        NominaRepositorioTxt repositorio = new NominaRepositorioTxt(archivo.toPath());

        Nomina nomina = repositorio.obtenerTodos().get(0);
        assertEquals(300000, nomina.getSalarioBaseOrdinario(), 0.01);
        assertEquals(0, nomina.getHorasExtra(), 0.01);
        assertEquals(0, nomina.getMontoHorasExtra(), 0.01);
    }
}
