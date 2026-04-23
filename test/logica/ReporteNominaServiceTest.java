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
        Nomina nomina = new Nomina();
        nomina.setId("NOM-001");
        nomina.setEmpleadoId("EMP-001");
        nomina.setNombreEmpleado("Ana Vargas");
        nomina.setPeriodo(YearMonth.of(2026, 4));
        nomina.setFechaGeneracion(LocalDate.of(2026, 4, 16));
        nomina.setSalarioBruto(1500000);
        nomina.setDeduccionSem(82500);
        nomina.setDeduccionIvm(64950);
        nomina.setDeduccionBancoPopular(15000);
        nomina.setDeduccionRenta(59840);
        nomina.setTotalDeducciones(222290);
        nomina.setAporteSemPatrono(138750);
        nomina.setAporteIvmPatrono(83700);
        nomina.setAporteAsignacionesFamiliares(75000);
        nomina.setAporteImas(7500);
        nomina.setAporteIna(22500);
        nomina.setAporteBancoPopularPatronal(3750);
        nomina.setAporteBancoPopularLpt(3750);
        nomina.setAporteFcl(22500);
        nomina.setAporteRop(30000);
        nomina.setAporteIns(15000);
        nomina.setTotalAportesPatronales(402950);
        nomina.setSalarioNeto(1277710);

        Path ruta = service.generarReporte(nomina);
        assertTrue(Files.exists(ruta));
        assertTrue(Files.size(ruta) > 0);
        String contenido = extraerTexto(ruta);
        assertTrue(contenido.contains("Comprobante individual de nomina"));
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
        nomina.setSalarioBruto(1500000);
        nomina.setDeduccionSem(82500);
        nomina.setDeduccionIvm(64950);
        nomina.setDeduccionBancoPopular(15000);
        nomina.setDeduccionRenta(59840);
        nomina.setTotalDeducciones(222290);
        nomina.setAporteSemPatrono(138750);
        nomina.setAporteIvmPatrono(83700);
        nomina.setAporteAsignacionesFamiliares(75000);
        nomina.setAporteImas(7500);
        nomina.setAporteIna(22500);
        nomina.setAporteBancoPopularPatronal(3750);
        nomina.setAporteBancoPopularLpt(3750);
        nomina.setAporteFcl(22500);
        nomina.setAporteRop(30000);
        nomina.setAporteIns(15000);
        nomina.setTotalAportesPatronales(402950);
        nomina.setSalarioNeto(1277710);
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
