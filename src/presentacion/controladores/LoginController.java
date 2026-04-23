package presentacion.controladores;

import entidades.Usuario;
import excepciones.AutenticacionFallidaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.awt.Color;
import presentacion.ContextoAplicacion;
import presentacion.LoginFrame;
import presentacion.MainFrame;
import utilidades.RegistroLogger;

/**
 * Controlador del flujo de autenticacion.
 */
public class LoginController {

    private final LoginFrame loginFrame;
    private final ContextoAplicacion contexto;

    public LoginController(LoginFrame loginFrame, ContextoAplicacion contexto) {
        this.loginFrame = loginFrame;
        this.contexto = contexto;
        this.loginFrame.setAccionIngresar(e -> autenticar());
        this.loginFrame.setAccionOlvidoContrasena(e -> recuperarContrasena());
        actualizarVisibilidadRecuperacion();
    }

    private void autenticar() {
        try {
            Usuario usuario = contexto.getAutenticacionService()
                    .autenticar(loginFrame.getUsuario(), loginFrame.getContrasena());
            loginFrame.mostrarEstado("Acceso concedido.", new Color(0, 128, 0));
            gestionarCredencialesPorDefecto(usuario);
            abrirPrincipal(usuario);
        } catch (AutenticacionFallidaException ex) {
            loginFrame.mostrarEstado(ex.getMessage(), new Color(180, 0, 0));
        } catch (PersistenciaException ex) {
            RegistroLogger.registrarError("Autenticacion", ex);
            loginFrame.mostrarEstado("No fue posible validar el usuario.", new Color(180, 0, 0));
        }
    }

    private void gestionarCredencialesPorDefecto(Usuario usuario) {
        if (!contexto.getAutenticacionService().usaCredencialesPorDefecto(usuario)) {
            return;
        }

        boolean deseaCambiar = loginFrame.mostrarAvisoCredencialesPorDefecto();
        if (!deseaCambiar) {
            loginFrame.mostrarMensaje(
                    "Puede cambiar el usuario y la contrase\u00f1a m\u00e1s tarde, pero se recomienda actualizarlos cuanto antes.");
            return;
        }

        while (true) {
            LoginFrame.SolicitudCredencialesAcceso solicitud =
                    loginFrame.solicitarActualizacionCredenciales(usuario.getUsername());
            if (solicitud == null) {
                loginFrame.mostrarMensaje(
                        "Actualizaci\u00f3n de credenciales pospuesta. Recuerde cambiar el usuario y la contrase\u00f1a por seguridad.");
                return;
            }

            try {
                contexto.getAutenticacionService().actualizarCredencialesIniciales(
                        usuario, solicitud.getNuevoUsername(), solicitud.getNuevaContrasena());
                loginFrame.mostrarMensaje(
                        "Credenciales actualizadas correctamente. Su nuevo usuario es: " + usuario.getUsername());
                actualizarVisibilidadRecuperacion();
                return;
            } catch (ValidacionException ex) {
                loginFrame.mostrarMensajeError(ex.getMessage());
            } catch (PersistenciaException ex) {
                RegistroLogger.registrarError("Cambio inicial de credenciales", ex);
                loginFrame.mostrarMensajeError(
                        "No fue posible actualizar las credenciales. Puede intentarlo m\u00e1s tarde.");
                return;
            }
        }
    }

    private void recuperarContrasena() {
        while (true) {
            LoginFrame.SolicitudRecuperacion solicitud = loginFrame.solicitarRecuperacionContrasena();
            if (solicitud == null) {
                return;
            }

            try {
                contexto.getAutenticacionService().restablecerContrasenaOlvidada(
                        solicitud.getUsername(), solicitud.getNombreCompleto(), solicitud.getNuevaContrasena());
                loginFrame.mostrarMensaje(
                        "Contrase\u00f1a restablecida correctamente. Ya puede ingresar con su nueva clave.");
                actualizarVisibilidadRecuperacion();
                return;
            } catch (ValidacionException ex) {
                loginFrame.mostrarMensajeError(ex.getMessage());
            } catch (PersistenciaException ex) {
                RegistroLogger.registrarError("Recuperacion de contrase\u00f1a", ex);
                loginFrame.mostrarMensajeError(
                        "No fue posible completar la recuperaci\u00f3n de contrase\u00f1a.");
                return;
            }
        }
    }

    private void actualizarVisibilidadRecuperacion() {
        try {
            boolean recuperacionVisible =
                    contexto.getAutenticacionService().debeMostrarRecuperacionContrasena();
            loginFrame.setRecuperacionVisible(recuperacionVisible);
            loginFrame.setCredencialesInicialesActivas(!recuperacionVisible);
        } catch (PersistenciaException ex) {
            RegistroLogger.registrarError("Visibilidad de recuperacion de contrase\u00f1a", ex);
            loginFrame.setRecuperacionVisible(false);
            loginFrame.setCredencialesInicialesActivas(true);
        }
    }

    private void abrirPrincipal(Usuario usuario) {
        MainFrame mainFrame = new MainFrame(usuario);
        NominaController nominaController = new NominaController(mainFrame.getNominaPanel(),
                contexto.getEmpleadoService(), contexto.getNominaService(),
                contexto.getReporteNominaService(), contexto.getCorreoService());
        new EmpleadoController(mainFrame.getEmpleadoPanel(), contexto.getEmpleadoService(),
                nominaController::recargarTodo);
        mainFrame.setVisible(true);
        loginFrame.dispose();
    }
}
