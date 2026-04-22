package presentacion.controladores;

import entidades.Empleado;
import entidades.Nomina;
import excepciones.CorreoException;
import excepciones.EntidadNoEncontradaException;
import excepciones.PdfException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.List;
import logica.CorreoService;
import logica.EmpleadoService;
import logica.NominaService;
import logica.ReporteNominaService;
import presentacion.NominaPanel;
import utilidades.RegistroLogger;

/**
 * Controlador del flujo de nómina, PDF y correo.
 */
public class NominaController {

    private final NominaPanel panel;
    private final EmpleadoService empleadoService;
    private final NominaService nominaService;
    private final ReporteNominaService reporteNominaService;
    private final CorreoService correoService;

    public NominaController(NominaPanel panel, EmpleadoService empleadoService, NominaService nominaService,
            ReporteNominaService reporteNominaService, CorreoService correoService) {
        this.panel = panel;
        this.empleadoService = empleadoService;
        this.nominaService = nominaService;
        this.reporteNominaService = reporteNominaService;
        this.correoService = correoService;
        configurarEventos();
        recargarTodo();
    }

    public final void recargarEmpleados() {
        try {
            panel.setEmpleados(empleadoService.listarActivos());
        } catch (PersistenciaException ex) {
            RegistroLogger.registrarError("Recargar empleados activos", ex);
            panel.mostrarError("No fue posible cargar los empleados activos.");
        }
    }

    public final void recargarTodo() {
        recargarEmpleados();
        recargarNominas();
    }

    private void configurarEventos() {
        panel.setAccionGenerar(e -> generarNomina());
        panel.setAccionExportarSeleccionada(e -> exportarSeleccionada());
        panel.setAccionExportarGeneral(e -> exportarGeneral());
        panel.setAccionEnviarCorreo(e -> enviarCorreo());
        panel.setAccionRecargar(e -> recargarTodo());
        panel.setAccionEliminar(e -> eliminarNomina());
        panel.setSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                panel.mostrarDetalleNomina(panel.getNominaSeleccionada());
            }
        });
    }

    private void generarNomina() {
        Empleado empleado = panel.getEmpleadoSeleccionado();
        YearMonth periodo = panel.getPeriodoSeleccionado();
        if (empleado == null) {
            panel.mostrarError("Debe seleccionar un empleado.");
            return;
        }
        try {
            Nomina nomina = nominaService.generarYGuardarNomina(empleado.getId(), periodo);
            Path rutaPdf = reporteNominaService.generarReporte(nomina);
            nomina.setRutaPdf(rutaPdf.toString());
            nominaService.actualizarRutaPdf(nomina.getId(), rutaPdf.toString());
            if (panel.isEnviarAutomatico()) {
                correoService.enviarNomina(empleado, nomina, rutaPdf);
            }
            recargarNominas();
            panel.mostrarInfo(panel.isEnviarAutomatico()
                    ? "Nómina generada, PDF creado y correo enviado."
                    : "Nómina generada y PDF creado correctamente.");
        } catch (PersistenciaException | ValidacionException | EntidadNoEncontradaException
                | PdfException | CorreoException ex) {
            RegistroLogger.registrarError("Generar nómina", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void exportarSeleccionada() {
        Nomina nomina = panel.getNominaSeleccionada();
        if (nomina == null) {
            panel.mostrarError("Seleccione una nómina del historial.");
            return;
        }
        try {
            Path ruta = asegurarPdf(nomina);
            recargarNominas();
            panel.mostrarInfo("PDF disponible en:\n" + ruta);
        } catch (PersistenciaException | PdfException | EntidadNoEncontradaException | ValidacionException ex) {
            RegistroLogger.registrarError("Exportar PDF individual", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void exportarGeneral() {
        try {
            YearMonth periodo = panel.getPeriodoSeleccionado();
            List<Nomina> nominas = nominaService.listarPorPeriodo(periodo);
            if (nominas.isEmpty()) {
                panel.mostrarError("No existen nóminas para el período seleccionado.");
                return;
            }
            Path ruta = reporteNominaService.generarReporteGeneral(nominas, periodo);
            panel.mostrarInfo("Reporte general generado en:\n" + ruta);
        } catch (PersistenciaException | PdfException ex) {
            RegistroLogger.registrarError("Exportar reporte general", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void enviarCorreo() {
        Nomina nomina = panel.getNominaSeleccionada();
        if (nomina == null) {
            panel.mostrarError("Seleccione una nómina del historial.");
            return;
        }
        try {
            Empleado empleado = empleadoService.buscarPorId(nomina.getEmpleadoId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró el empleado asociado."));
            Path ruta = asegurarPdf(nomina);
            correoService.enviarNomina(empleado, nomina, ruta);
            panel.mostrarInfo("Correo enviado correctamente a " + empleado.getCorreoElectronico());
        } catch (PersistenciaException | EntidadNoEncontradaException | PdfException
                | CorreoException | ValidacionException ex) {
            RegistroLogger.registrarError("Enviar correo de nómina", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void eliminarNomina() {
        Nomina nomina = panel.getNominaSeleccionada();
        if (nomina == null) {
            panel.mostrarError("Seleccione una nómina del historial para eliminar.");
            return;
        }
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                panel,
                "¿Está seguro de que desea eliminar la nómina de " + nomina.getNombreEmpleado()
                + " del período " + utilidades.FormatoUtil.formatearPeriodo(nomina.getPeriodo()) + "?\n"
                + "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        if (confirmacion != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        try {
            nominaService.eliminarNomina(nomina.getId());
            recargarNominas();
            panel.mostrarInfo("Nómina eliminada correctamente.");
        } catch (PersistenciaException | EntidadNoEncontradaException ex) {
            RegistroLogger.registrarError("Eliminar nómina", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void recargarNominas() {
        try {
            panel.setNominas(nominaService.listarNominas());
            panel.mostrarDetalleNomina(panel.getNominaSeleccionada());
        } catch (PersistenciaException ex) {
            RegistroLogger.registrarError("Listar nóminas", ex);
            panel.mostrarError("No fue posible cargar el historial de nóminas.");
        }
    }

    private Path asegurarPdf(Nomina nomina)
            throws PdfException, PersistenciaException, EntidadNoEncontradaException, ValidacionException {
        if (nomina.getRutaPdf() != null && !nomina.getRutaPdf().isBlank()) {
            Path rutaExistente = Paths.get(nomina.getRutaPdf());
            if (Files.exists(rutaExistente)) {
                return rutaExistente;
            }
        }
        Path rutaNueva = reporteNominaService.generarReporte(nomina);
        nomina.setRutaPdf(rutaNueva.toString());
        nominaService.actualizarRutaPdf(nomina.getId(), rutaNueva.toString());
        return rutaNueva;
    }
}
