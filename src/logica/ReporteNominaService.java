package logica;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entidades.Nomina;
import excepciones.PdfException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.YearMonth;
import java.util.List;
import utilidades.ConstantesNomina;
import utilidades.FormatoUtil;
import utilidades.RutasSistema;

/**
 * Genera reportes PDF individuales y generales de planilla.
 */
public class ReporteNominaService extends LogicaBase implements ExportadorPdf<Nomina> {

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.DARK_GRAY);
    private static final Font TEXTO = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

    @Override
    public Path generarReporte(Nomina nomina) throws PdfException {
        String archivo = "nomina_" + sanitizar(nomina.getNombreEmpleado()) + "_" + nomina.getPeriodo() + ".pdf";
        Path ruta = RutasSistema.REPORTES_DIR.resolve(archivo);
        try (OutputStream output = Files.newOutputStream(ruta, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Document document = new Document(PageSize.A4, 40, 40, 36, 36);
            PdfWriter.getInstance(document, output);
            document.open();
            document.add(new Paragraph("Sistema de Nomina Empresarial", TITULO));
            document.add(new Paragraph("Comprobante individual de nomina", SUBTITULO));
            document.add(new Paragraph("Empleado: " + nomina.getNombreEmpleado(), TEXTO));
            document.add(new Paragraph("Periodo: " + FormatoUtil.formatearPeriodo(nomina.getPeriodo()), TEXTO));
            document.add(new Paragraph("Fecha de generacion: " + FormatoUtil.formatearFecha(nomina.getFechaGeneracion()), TEXTO));
            document.add(new Paragraph(" "));
            document.add(crearTablaConceptosTrabajador(nomina));
            document.add(new Paragraph(" "));
            document.add(crearTablaAportesPatronales(nomina));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Fuentes normativas 2026:", SUBTITULO));
            document.add(new Paragraph("CCSS IVM y contribuciones: " + ConstantesNomina.FUENTE_CCSS, TEXTO));
            document.add(new Paragraph("Hacienda tramos renta salarios: " + ConstantesNomina.FUENTE_HACIENDA, TEXTO));
            document.close();
            return ruta;
        } catch (IOException | DocumentException ex) {
            throw new PdfException("No fue posible generar el PDF individual.", ex);
        }
    }

    /**
     * Genera un reporte consolidado de nominas por periodo.
     *
     * @param nominas nominas incluidas
     * @param periodo periodo del reporte
     * @return ruta del archivo generado
     * @throws PdfException si falla la generacion
     */
    public Path generarReporteGeneral(List<Nomina> nominas, YearMonth periodo) throws PdfException {
        String archivo = "reporte_general_" + periodo + ".pdf";
        Path ruta = RutasSistema.REPORTES_DIR.resolve(archivo);
        try (OutputStream output = Files.newOutputStream(ruta, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 30, 30);
            PdfWriter.getInstance(document, output);
            document.open();
            document.add(new Paragraph("Reporte General de Nomina", TITULO));
            document.add(new Paragraph("Periodo: " + FormatoUtil.formatearPeriodo(periodo), SUBTITULO));
            document.add(new Paragraph("Cantidad de colaboradores liquidados: " + nominas.size(), TEXTO));
            document.add(new Paragraph(" "));
            document.add(crearTablaGeneral(nominas));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Totales del periodo", SUBTITULO));
            document.add(new Paragraph("Salario bruto total: " + FormatoUtil.formatearMoneda(
                    nominas.stream().mapToDouble(Nomina::getSalarioBruto).sum()), TEXTO));
            document.add(new Paragraph("Deducciones totales: " + FormatoUtil.formatearMoneda(
                    nominas.stream().mapToDouble(Nomina::getTotalDeducciones).sum()), TEXTO));
            document.add(new Paragraph("Aportes patronales totales: " + FormatoUtil.formatearMoneda(
                    nominas.stream().mapToDouble(Nomina::getTotalAportesPatronales).sum()), TEXTO));
            document.add(new Paragraph("Salario neto total: " + FormatoUtil.formatearMoneda(
                    nominas.stream().mapToDouble(Nomina::getSalarioNeto).sum()), TEXTO));
            document.close();
            return ruta;
        } catch (IOException | DocumentException ex) {
            throw new PdfException("No fue posible generar el reporte general PDF.", ex);
        }
    }

    private PdfPTable crearTablaConceptosTrabajador(Nomina nomina) {
        PdfPTable table = new PdfPTable(new float[]{3.5f, 1.5f});
        table.setWidthPercentage(100);
        agregarEncabezado(table, "Concepto");
        agregarEncabezado(table, "Monto");
        agregarFila(table, "Salario bruto", FormatoUtil.formatearMoneda(nomina.getSalarioBruto()));
        agregarFila(table, "Deduccion SEM", FormatoUtil.formatearMoneda(nomina.getDeduccionSem()));
        agregarFila(table, "Deduccion IVM", FormatoUtil.formatearMoneda(nomina.getDeduccionIvm()));
        agregarFila(table, "Banco Popular trabajador", FormatoUtil.formatearMoneda(nomina.getDeduccionBancoPopular()));
        agregarFila(table, "Impuesto sobre la renta", FormatoUtil.formatearMoneda(nomina.getDeduccionRenta()));
        agregarFila(table, "Total deducciones", FormatoUtil.formatearMoneda(nomina.getTotalDeducciones()));
        agregarFila(table, "Salario neto", FormatoUtil.formatearMoneda(nomina.getSalarioNeto()));
        return table;
    }

    private PdfPTable crearTablaAportesPatronales(Nomina nomina) {
        PdfPTable table = new PdfPTable(new float[]{3.5f, 1.5f});
        table.setWidthPercentage(100);
        agregarEncabezado(table, "Aporte patronal");
        agregarEncabezado(table, "Monto");
        agregarFila(table, "SEM patrono", FormatoUtil.formatearMoneda(nomina.getAporteSemPatrono()));
        agregarFila(table, "IVM patrono", FormatoUtil.formatearMoneda(nomina.getAporteIvmPatrono()));
        agregarFila(table, "Asignaciones familiares", FormatoUtil.formatearMoneda(nomina.getAporteAsignacionesFamiliares()));
        agregarFila(table, "IMAS", FormatoUtil.formatearMoneda(nomina.getAporteImas()));
        agregarFila(table, "INA", FormatoUtil.formatearMoneda(nomina.getAporteIna()));
        agregarFila(table, "Banco Popular patronal", FormatoUtil.formatearMoneda(nomina.getAporteBancoPopularPatronal()));
        agregarFila(table, "Banco Popular LPT", FormatoUtil.formatearMoneda(nomina.getAporteBancoPopularLpt()));
        agregarFila(table, "FCL", FormatoUtil.formatearMoneda(nomina.getAporteFcl()));
        agregarFila(table, "ROP", FormatoUtil.formatearMoneda(nomina.getAporteRop()));
        agregarFila(table, "INS", FormatoUtil.formatearMoneda(nomina.getAporteIns()));
        agregarFila(table, "Total aportes patronales", FormatoUtil.formatearMoneda(nomina.getTotalAportesPatronales()));
        return table;
    }

    private PdfPTable crearTablaGeneral(List<Nomina> nominas) {
        PdfPTable table = new PdfPTable(new float[]{3.5f, 2f, 2f, 2f, 2f});
        table.setWidthPercentage(100);
        agregarEncabezado(table, "Empleado");
        agregarEncabezado(table, "Bruto");
        agregarEncabezado(table, "Deducciones");
        agregarEncabezado(table, "Aportes patronales");
        agregarEncabezado(table, "Neto");
        for (Nomina nomina : nominas) {
            agregarFila(table, nomina.getNombreEmpleado(), FormatoUtil.formatearMoneda(nomina.getSalarioBruto()),
                    FormatoUtil.formatearMoneda(nomina.getTotalDeducciones()),
                    FormatoUtil.formatearMoneda(nomina.getTotalAportesPatronales()),
                    FormatoUtil.formatearMoneda(nomina.getSalarioNeto()));
        }
        return table;
    }

    private void agregarEncabezado(PdfPTable table, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, SUBTITULO));
        cell.setBackgroundColor(new BaseColor(230, 230, 230));
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void agregarFila(PdfPTable table, String... valores) {
        for (String valor : valores) {
            PdfPCell cell = new PdfPCell(new Phrase(valor, TEXTO));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private String sanitizar(String valor) {
        return valor.toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+", "")
                .replaceAll("_+$", "");
    }
}
