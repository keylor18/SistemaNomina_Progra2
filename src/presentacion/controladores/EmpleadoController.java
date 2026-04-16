package presentacion.controladores;

import entidades.Empleado;
import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.time.format.DateTimeParseException;
import presentacion.EmpleadoPanel;
import logica.EmpleadoService;
import utilidades.RegistroLogger;

/**
 * Controlador de mantenimiento de empleados.
 */
public class EmpleadoController {

    private final EmpleadoPanel panel;
    private final EmpleadoService empleadoService;
    private final Runnable postActualizacion;

    public EmpleadoController(EmpleadoPanel panel, EmpleadoService empleadoService, Runnable postActualizacion) {
        this.panel = panel;
        this.empleadoService = empleadoService;
        this.postActualizacion = postActualizacion;
        configurarEventos();
        recargar();
    }

    private void configurarEventos() {
        panel.setAccionGuardar(e -> guardar());
        panel.setAccionActualizar(e -> actualizar());
        panel.setAccionEliminar(e -> eliminar());
        panel.setAccionLimpiar(e -> panel.limpiarFormulario());
        panel.setAccionRecargar(e -> recargar());
        panel.setSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Empleado seleccionado = panel.getEmpleadoSeleccionado();
                if (seleccionado != null) {
                    panel.cargarEmpleadoEnFormulario(seleccionado);
                }
            }
        });
    }

    private void guardar() {
        try {
            Empleado empleado = panel.construirEmpleadoDesdeFormulario();
            empleadoService.crearEmpleado(empleado);
            panel.mostrarInfo("Empleado registrado correctamente.");
            panel.limpiarFormulario();
            recargar();
            notificarCambio();
        } catch (NumberFormatException | DateTimeParseException ex) {
            panel.mostrarError("Revise el salario y la fecha de ingreso.");
        } catch (PersistenciaException | ValidacionException ex) {
            RegistroLogger.registrarError("Guardar empleado", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void actualizar() {
        try {
            Empleado empleado = panel.construirEmpleadoDesdeFormulario();
            empleadoService.actualizarEmpleado(empleado);
            panel.mostrarInfo("Empleado actualizado correctamente.");
            panel.limpiarFormulario();
            recargar();
            notificarCambio();
        } catch (NumberFormatException | DateTimeParseException ex) {
            panel.mostrarError("Revise el salario y la fecha de ingreso.");
        } catch (PersistenciaException | ValidacionException | EntidadNoEncontradaException ex) {
            RegistroLogger.registrarError("Actualizar empleado", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        Empleado empleado = panel.getEmpleadoSeleccionado();
        if (empleado == null) {
            panel.mostrarError("Seleccione un empleado para eliminar.");
            return;
        }
        try {
            empleadoService.eliminarEmpleado(empleado.getId());
            panel.mostrarInfo("Empleado eliminado correctamente.");
            panel.limpiarFormulario();
            recargar();
            notificarCambio();
        } catch (PersistenciaException | EntidadNoEncontradaException ex) {
            RegistroLogger.registrarError("Eliminar empleado", ex);
            panel.mostrarError(ex.getMessage());
        }
    }

    private void recargar() {
        try {
            panel.setEmpleados(empleadoService.listarEmpleados());
        } catch (PersistenciaException ex) {
            RegistroLogger.registrarError("Listar empleados", ex);
            panel.mostrarError("No fue posible cargar los empleados.");
        }
    }

    private void notificarCambio() {
        if (postActualizacion != null) {
            postActualizacion.run();
        }
    }
}
