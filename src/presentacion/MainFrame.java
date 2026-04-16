package presentacion;

import entidades.Usuario;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import presentacion.componentes.PanelGradiente;
import presentacion.componentes.PanelRedondeado;
import presentacion.estilo.TemaVisual;
import utilidades.FormatoUtil;

/**
 * Ventana principal del sistema.
 */
public class MainFrame extends JFrame {

    private final EmpleadoPanel empleadoPanel;
    private final NominaPanel nominaPanel;

    public MainFrame(Usuario usuario) {
        setTitle("Sistema de Nomina Empresarial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1260, 820));
        setSize(1320, 860);
        setLocationRelativeTo(null);

        empleadoPanel = new EmpleadoPanel();
        nominaPanel = new NominaPanel();

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(TemaVisual.FONDO_APP);
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.add(crearEncabezado(usuario), BorderLayout.NORTH);
        root.add(crearCentro(), BorderLayout.CENTER);
        root.add(crearPie(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    public EmpleadoPanel getEmpleadoPanel() {
        return empleadoPanel;
    }

    public NominaPanel getNominaPanel() {
        return nominaPanel;
    }

    private Component crearEncabezado(Usuario usuario) {
        PanelGradiente encabezado = new PanelGradiente(new java.awt.Color(18, 93, 95), new java.awt.Color(11, 55, 56));
        encabezado.setLayout(new BorderLayout(16, 16));
        encabezado.setBorder(BorderFactory.createEmptyBorder(26, 28, 26, 28));

        JPanel bloqueTitulo = new JPanel();
        bloqueTitulo.setOpaque(false);
        bloqueTitulo.setLayout(new BoxLayout(bloqueTitulo, BoxLayout.Y_AXIS));

        JLabel nombre = new JLabel("Sistema de Nomina Empresarial");
        nombre.setFont(TemaVisual.fuente(Font.BOLD, 28));
        nombre.setForeground(java.awt.Color.WHITE);

        JLabel subtitulo = new JLabel("Operacion administrativa, calculo de planilla, reportes y trazabilidad.");
        subtitulo.setFont(TemaVisual.fuente(Font.PLAIN, 14));
        subtitulo.setForeground(new java.awt.Color(222, 234, 231));

        bloqueTitulo.add(nombre);
        bloqueTitulo.add(Box.createVerticalStrut(8));
        bloqueTitulo.add(subtitulo);

        JPanel bloqueUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        bloqueUsuario.setOpaque(false);
        bloqueUsuario.add(crearTarjetaInfo("Usuario", usuario.getNombreCompleto()));
        bloqueUsuario.add(crearTarjetaInfo("Rol", usuario.getRol().name()));
        bloqueUsuario.add(crearTarjetaInfo("Fecha", FormatoUtil.formatearFecha(LocalDate.now())));

        encabezado.add(bloqueTitulo, BorderLayout.CENTER);
        encabezado.add(bloqueUsuario, BorderLayout.EAST);
        return encabezado;
    }

    private Component crearCentro() {
        PanelRedondeado contenedor = new PanelRedondeado(TemaVisual.SUPERFICIE, 28);
        contenedor.setLayout(new BorderLayout());
        contenedor.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JTabbedPane tabs = new JTabbedPane();
        TemaVisual.estilizarTabs(tabs);
        tabs.addTab("Colaboradores", empleadoPanel);
        tabs.addTab("Nomina y reportes", nominaPanel);
        contenedor.add(tabs, BorderLayout.CENTER);
        return contenedor;
    }

    private Component crearPie() {
        PanelRedondeado pie = new PanelRedondeado(TemaVisual.SUPERFICIE_SECUNDARIA, 22);
        pie.setLayout(new BorderLayout());
        pie.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel texto = new JLabel("Base de trabajo: normativa Costa Rica 2026, persistencia local, PDF y JavaMail.");
        texto.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        texto.setForeground(TemaVisual.TEXTO_SUAVE);
        pie.add(texto, BorderLayout.WEST);
        return pie;
    }

    private Component crearTarjetaInfo(String titulo, String valor) {
        PanelRedondeado tarjeta = new PanelRedondeado(new java.awt.Color(255, 255, 255, 30), 22,
                new java.awt.Color(255, 255, 255, 40));
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(TemaVisual.fuente(Font.BOLD, 11));
        lblTitulo.setForeground(new java.awt.Color(230, 237, 234));
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(TemaVisual.fuente(Font.BOLD, 13));
        lblValor.setForeground(java.awt.Color.WHITE);
        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(4));
        contenido.add(lblValor);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }
}
