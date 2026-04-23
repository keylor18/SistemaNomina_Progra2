package logica;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import entidades.Nomina;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.Test;
import utilidades.RutasSistema;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Pruebas de generacion de PDF.
 */
public class ReporteNominaServiceTest {

    @Test
    public void debeGenerarPdfIndividual() throws Exception {
        Files.createDirectories(RutasSistema.REPORTES_DIR);
        ReporteNominaService service = new ReporteNominaService();
        Nomina nomina = crearNominaBase();

        Path ruta = service.generarReporte(nomina);
        assertTrue(Files.exists(ruta));
        assertTrue(Files.size(ruta) > 0);
        String contenido = extraerTexto(ruta);
        assertTrue(contenido.contains("Comprobante individual de nomina"));
        assertTrue(contenido.contains("Horas extra registradas"));
        assertFalse(contenido.contains("Total aportes patronales"));
    }

    @Test
    public void debeGenerarPdfPatronalSeparado() throws Exception {
        Files.createDirectories(RutasSistema.REPORTES_DIR);
        ReporteNominaService service = new ReporteNominaService();
        Nomina nomina = crearNominaBase();

        Path ruta = service.generarReportePatronal(nomina);
        assertTrue(Files.exists(ruta));
        assertTrue(Files.size(ruta) > 0);
        String contenido = extraerTexto(ruta);
        assertTrue(contenido.contains("Reporte patronal separado"));
        assertTrue(contenido.contains("Horas extra liquidadas"));
        assertTrue(contenido.contains("Total aportes patronales"));
        assertTrue(contenido.contains("Costo total empresa"));
    }

    private Nomina crearNominaBase() {
        Nomina nomina = new Nomina();
        nomina.setId("NOM-001");
        nomina.setEmpleadoId("EMP-001");
        nomina.setNombreEmpleado("Ana Vargas");
        nomina.setPeriodo(YearMonth.of(2026, 4));
        nomina.setFechaGeneracion(LocalDate.of(2026, 4, 16));
        nomina.setSalarioBaseOrdinario(1_500_000);
        nomina.setHorasExtra(8);
        nomina.setMontoHorasExtra(75_000);
        nomina.setSalarioBruto(1_575_000);
        nomina.setDeduccionSem(82_500);
        nomina.setDeduccionIvm(64_950);
        nomina.setDeduccionBancoPopular(15_750);
        nomina.setDeduccionRenta(59_840);
        nomina.setTotalDeducciones(223_040);
        nomina.setAporteSemPatrono(145_687.50);
        nomina.setAporteIvmPatrono(87_885);
        nomina.setAporteAsignacionesFamiliares(78_750);
        nomina.setAporteImas(7_875);
        nomina.setAporteIna(23_625);
        nomina.setAporteBancoPopularPatronal(3_937.50);
        nomina.setAporteBancoPopularLpt(3_937.50);
        nomina.setAporteFcl(23_625);
        nomina.setAporteRop(31_500);
        nomina.setAporteIns(15_750);
        nomina.setTotalAportesPatronales(422_572.50);
        nomina.setSalarioNeto(1_351_960);
        return nomina;
    }

    private String extraerTexto(Path ruta) throws Exception {
        PdfReader reader = new PdfReader(ruta.toString());
        try {
            StringBuilder contenido = new StringBuilder();
            for (int pagina = 1; pagina <= reader.getNumberOfPages(); pagina++) {
                contenido.append(PdfTextExtractor.getTextFromPage(reader, pagina));
            }
            return contenido.toString();
        } finally {
            reader.close();
        }
    }
}
