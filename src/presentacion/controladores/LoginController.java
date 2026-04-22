package presentacion.controladores;

import entidades.Usuario;
import excepciones.AutenticacionFallidaException;
import excepciones.PersistenciaException;
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
    }

    private void autenticar() {
        try {
            Usuario usuario = contexto.getAutenticacionService()
                    .autenticar(loginFrame.getUsuario(), loginFrame.getContrasena());
            loginFrame.mostrarEstado("Acceso concedido.", new Color(0, 128, 0));
            abrirPrincipal(usuario);
        } catch (AutenticacionFallidaException ex) {
            loginFrame.mostrarEstado(ex.getMessage(), new Color(180, 0, 0));
        } catch (PersistenciaException ex) {
            RegistroLogger.registrarError("Autenticacion", ex);
            loginFrame.mostrarEstado("No fue posible validar el usuario.", new Color(180, 0, 0));
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
