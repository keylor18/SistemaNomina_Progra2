package presentacion;

import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.awt.EventQueue;
import javax.swing.JOptionPane;
import presentacion.controladores.LoginController;
import presentacion.estilo.TemaVisual;
import utilidades.RegistroLogger;
import utilidades.SistemaArchivosUtil;

/**
 * Punto de entrada principal del proyecto Examen2_Programa2.
 */
public final class Examen2_Programa2 {

    private Examen2_Programa2() {
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TemaVisual.instalarTema();
                SistemaArchivosUtil.inicializarEstructura();
                ContextoAplicacion contexto = new ContextoAplicacion();
                LoginFrame loginFrame = new LoginFrame();
                new LoginController(loginFrame, contexto);
                loginFrame.setVisible(true);
            } catch (PersistenciaException | ValidacionException ex) {
                RegistroLogger.registrarError("Inicio del sistema", ex);
                JOptionPane.showMessageDialog(null,
                        "No fue posible iniciar el sistema:\n" + ex.getMessage(),
                        "Error de inicio", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                RegistroLogger.registrarError("Inicio inesperado del sistema", ex);
                JOptionPane.showMessageDialog(null,
                        "Ocurrio un error inesperado al iniciar la aplicacion.",
                        "Error de inicio", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
