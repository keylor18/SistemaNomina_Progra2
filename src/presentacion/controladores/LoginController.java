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
 * Controlador del flujo de autenticación.
 */
public class LoginController {

    private final LoginFrame loginFrame;
    private final ContextoAplicacion contexto;

    public LoginController(LoginFrame loginFrame, ContextoAplicacion contexto) {
        this.loginFrame = loginFrame;
        this.contexto = contexto;
        this.loginFrame.setAccionIngresar(e -> autenticar());
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
            RegistroLogger.registrarError("Autenticación", ex);
            loginFrame.mostrarEstado("No fue posible validar el usuario.", new Color(180, 0, 0));
        }
    }

    private void gestionarCredencialesPorDefecto(Usuario usuario) {
        if (!contexto.getAutenticacionService().usaCredencialesPorDefecto(usuario)) {
            return;
        }
        boolean deseaCambiar = loginFrame.mostrarAvisoCredencialesPorDefecto();
        if (!deseaCambiar) {
            loginFrame.mostrarMensaje("Puede cambiar la contraseña más tarde, pero se recomienda actualizarla cuanto antes.");
            return;
        }

        while (true) {
            String nuevaContrasena = loginFrame.solicitarNuevaContrasenaSegura();
            if (nuevaContrasena == null) {
                loginFrame.mostrarMensaje("Cambio de contraseña pospuesto. Recuerde actualizar las credenciales por seguridad.");
                return;
            }
            try {
                contexto.getAutenticacionService().cambiarContrasena(usuario, nuevaContrasena);
                loginFrame.mostrarMensaje("Contraseña actualizada correctamente.");
                return;
            } catch (ValidacionException ex) {
                loginFrame.mostrarMensajeError(ex.getMessage());
            } catch (PersistenciaException ex) {
                RegistroLogger.registrarError("Cambio inicial de contraseña", ex);
                loginFrame.mostrarMensajeError("No fue posible actualizar la contraseña. Puede intentarlo más tarde.");
                return;
            }
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
