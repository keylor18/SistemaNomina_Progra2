package app;

import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.awt.EventQueue;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import presentacion.LoginFrame;
import presentacion.controladores.LoginController;
import utilidades.RegistroLogger;
import utilidades.SistemaArchivosUtil;

/**
 * Punto de entrada de la aplicacion.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SistemaArchivosUtil.inicializarEstructura();
                AplicacionContexto contexto = new AplicacionContexto();
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
