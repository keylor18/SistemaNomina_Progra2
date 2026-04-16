package presentacion;

import entidades.Usuario;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 * Ventana principal del sistema.
 */
public class MainFrame extends JFrame {

    private final EmpleadoPanel empleadoPanel;
    private final NominaPanel nominaPanel;

    public MainFrame(Usuario usuario) {
        setTitle("Sistema de Nomina Empresarial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);

        JLabel encabezado = new JLabel(
                "Bienvenido, " + usuario.getNombreCompleto() + " | Rol: " + usuario.getRol(),
                SwingConstants.CENTER);
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 18));

        empleadoPanel = new EmpleadoPanel();
        nominaPanel = new NominaPanel();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Gestion de Empleados", empleadoPanel);
        tabs.addTab("Nomina y Reportes", nominaPanel);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.add(encabezado, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);
    }

    public EmpleadoPanel getEmpleadoPanel() {
        return empleadoPanel;
    }

    public NominaPanel getNominaPanel() {
        return nominaPanel;
    }
}
