package presentacion;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import presentacion.componentes.PanelRedondeado;
import presentacion.estilo.TemaVisual;

/**
 * Pantalla de autenticacion del sistema.
 */
public class LoginFrame extends JFrame {

    private static final Color FONDO_LOGIN = new Color(19, 91, 86);
    private static final Color CIRCULO_SUAVE = new Color(255, 255, 255, 20);
    private static final Color CIRCULO_MAS_SUAVE = new Color(255, 255, 255, 14);
    private static final Color BORDE_TARJETA = new Color(236, 240, 239);
    private static final Color TEXTO_TITULO = new Color(18, 80, 82);
    private static final Color TEXTO_SECUNDARIO = new Color(156, 149, 148);
    private static final Color TEXTO_ETIQUETA = new Color(145, 140, 140);
    private static final Color BORDE_CAMPO = new Color(216, 220, 222);
    private static final Color ICONO_CAMPO = new Color(173, 176, 178);
    private static final Color BOTON_PRIMARIO = new Color(19, 85, 82);
    private static final Color FONDO_INFO = new Color(247, 252, 251);
    private static final Color BORDE_INFO = new Color(129, 199, 194);
    private static final Color TEXTO_INFO = new Color(49, 135, 137);
    private static final int ANCHO_CONTENIDO = 336;
    private static final int ALTURA_MINIMA_PANEL_INFO = 64;
    private static final int ANCHO_TARJETA = 402;
    private static final int ANCHO_VENTANA = 920;
    private static final int ALTURA_VENTANA = 640;
    private static final int ANCHO_MINIMO_VENTANA = 860;
    private static final int ALTURA_MINIMA_VENTANA = 620;

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblEstado;

    public LoginFrame() {
        setTitle("SistemaNomina_Progra2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(ANCHO_VENTANA, ALTURA_VENTANA));
        setMinimumSize(new Dimension(ANCHO_MINIMO_VENTANA, ALTURA_MINIMA_VENTANA));
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel fondo = new FondoDecoradoPanel();
        fondo.setLayout(new GridBagLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        fondo.setFocusable(true);

        JPanel panelAcceso = crearPanelAcceso();
        PanelRedondeado tarjetaPrincipal = new PanelRedondeado(Color.WHITE, 34, BORDE_TARJETA);
        tarjetaPrincipal.setLayout(new BorderLayout());
        tarjetaPrincipal.setBorder(new EmptyBorder(40, 32, 28, 32));
        tarjetaPrincipal.add(panelAcceso, BorderLayout.CENTER);

        Dimension tamanoTarjeta = calcularTamanoTarjeta(panelAcceso, tarjetaPrincipal);
        tarjetaPrincipal.setPreferredSize(tamanoTarjeta);
        tarjetaPrincipal.setMinimumSize(tamanoTarjeta);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        fondo.add(tarjetaPrincipal, constraints);

        setContentPane(fondo);
        getRootPane().setDefaultButton(btnIngresar);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                fondo.requestFocusInWindow();
            }
        });
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getContrasena() {
        return new String(txtContrasena.getPassword());
    }

    public void setAccionIngresar(ActionListener listener) {
        btnIngresar.addActionListener(listener);
    }

    public void mostrarEstado(String mensaje, Color color) {
        boolean visible = mensaje != null && !mensaje.isBlank();
        lblEstado.setVisible(visible);
        if (visible) {
            lblEstado.setForeground(color);
            lblEstado.setText(mensaje);
        } else {
            lblEstado.setText(" ");
        }
        lblEstado.revalidate();
        lblEstado.repaint();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private JPanel crearPanelAcceso() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComponent icono = new IconoCabeceraUsuarios();
        icono.setAlignmentX(CENTER_ALIGNMENT);
        icono.setMaximumSize(icono.getPreferredSize());

        JLabel titulo = new JLabel("Sistema Nómina");
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setFont(TemaVisual.fuente(Font.BOLD, 19));
        titulo.setForeground(TEXTO_TITULO);

        JLabel subtitulo = new JLabel("Gestión de planilla y empleados");
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);
        subtitulo.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        subtitulo.setForeground(TEXTO_SECUNDARIO);

        txtUsuario = new JTextField("admin");
        txtContrasena = new JPasswordField("Admin123");
        txtContrasena.setEchoChar('\u2022');
        prepararCampo(txtUsuario);
        prepararCampo(txtContrasena);

        btnIngresar = new BotonPrimario("Ingresar al panel");
        btnIngresar.setAlignmentX(CENTER_ALIGNMENT);
        btnIngresar.setPreferredSize(new Dimension(ANCHO_CONTENIDO, 42));
        btnIngresar.setMaximumSize(btnIngresar.getPreferredSize());

        lblEstado = new JLabel(" ");
        lblEstado.setAlignmentX(CENTER_ALIGNMENT);
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        lblEstado.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        lblEstado.setVisible(false);

        panel.add(icono);
        panel.add(Box.createVerticalStrut(16));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(32));
        panel.add(centrarComponente(crearBloqueCampo("USUARIO", txtUsuario, TipoIcono.USUARIO)));
        panel.add(Box.createVerticalStrut(16));
        panel.add(centrarComponente(crearBloqueCampo("CONTRASEÑA", txtContrasena, TipoIcono.CANDADO)));
        panel.add(Box.createVerticalStrut(28));
        panel.add(centrarComponente(btnIngresar));
        panel.add(Box.createVerticalStrut(24));
        panel.add(centrarComponente(lblEstado));
        panel.add(Box.createVerticalStrut(16));
        panel.add(centrarComponente(crearPanelCredenciales()));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JComponent crearBloqueCampo(String etiqueta, JTextComponent campo, TipoIcono tipoIcono) {
        JPanel bloque = new JPanel();
        bloque.setOpaque(false);
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
        bloque.setPreferredSize(new Dimension(ANCHO_CONTENIDO, 67));
        bloque.setMaximumSize(bloque.getPreferredSize());

        JLabel label = new JLabel(etiqueta);
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.setFont(TemaVisual.fuente(Font.PLAIN, 11));
        label.setForeground(TEXTO_ETIQUETA);

        CampoIconoPanel campoVisual = new CampoIconoPanel(campo, tipoIcono);
        campoVisual.setAlignmentX(LEFT_ALIGNMENT);
        campoVisual.setPreferredSize(new Dimension(ANCHO_CONTENIDO, 38));
        campoVisual.setMaximumSize(campoVisual.getPreferredSize());

        bloque.add(label);
        bloque.add(Box.createVerticalStrut(7));
        bloque.add(campoVisual);
        return bloque;
    }

    private JComponent crearPanelCredenciales() {
        PanelCredenciales panel = new PanelCredenciales();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(11, 14, 11, 14));

        JComponent icono = new IconoCampo(TipoIcono.INFO, new Color(24, 106, 101));
        icono.setPreferredSize(new Dimension(18, 18));
        panel.add(icono, BorderLayout.WEST);

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Credenciales iniciales");
        titulo.setFont(TemaVisual.fuente(Font.PLAIN, 11));
        titulo.setForeground(TEXTO_TITULO);

        String color = TemaVisual.colorHex(TEXTO_INFO);
        JLabel detalle = new JLabel("<html>Usuario: <font color='" + color + "'><b>admin</b></font>"
                + " &nbsp;-&nbsp; Contraseña: <font color='" + color + "'><b>Admin123</b></font></html>");
        detalle.setFont(TemaVisual.fuente(Font.PLAIN, 11));
        detalle.setForeground(TEXTO_SECUNDARIO);

        texto.add(titulo);
        texto.add(Box.createVerticalStrut(2));
        texto.add(detalle);
        panel.add(texto, BorderLayout.CENTER);

        Dimension tamanoPreferido = panel.getPreferredSize();
        Dimension tamanoFinal = new Dimension(ANCHO_CONTENIDO,
                Math.max(ALTURA_MINIMA_PANEL_INFO, tamanoPreferido.height));
        panel.setPreferredSize(tamanoFinal);
        panel.setMinimumSize(tamanoFinal);
        panel.setMaximumSize(tamanoFinal);
        return panel;
    }

    private Dimension calcularTamanoTarjeta(JComponent contenido, JComponent tarjeta) {
        Dimension tamanoContenido = contenido.getPreferredSize();
        Insets insets = tarjeta.getInsets();
        return new Dimension(Math.max(ANCHO_TARJETA, tamanoContenido.width + insets.left + insets.right),
                tamanoContenido.height + insets.top + insets.bottom);
    }

    private JComponent centrarComponente(JComponent componente) {
        JPanel envoltura = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        envoltura.setOpaque(false);
        envoltura.setAlignmentX(CENTER_ALIGNMENT);
        envoltura.add(componente);
        return envoltura;
    }

    private void prepararCampo(JTextComponent campo) {
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createEmptyBorder());
        campo.setForeground(new Color(59, 69, 74));
        campo.setCaretColor(TEXTO_TITULO);
        campo.setFont(TemaVisual.fuente(Font.PLAIN, 14));
    }

    private enum TipoIcono {
        USUARIO,
        CANDADO,
        INFO
    }

    private static final class FondoDecoradoPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(FONDO_LOGIN);
            g2.fillRect(0, 0, getWidth(), getHeight());

            pintarCirculo(g2, -0.12, -0.14, 0.48, CIRCULO_SUAVE);
            pintarCirculo(g2, 0.80, 0.10, 0.16, CIRCULO_MAS_SUAVE);
            pintarCirculo(g2, 0.12, 0.74, 0.10, CIRCULO_MAS_SUAVE);
            pintarCirculo(g2, 0.78, 0.67, 0.34, CIRCULO_MAS_SUAVE);
            g2.dispose();
        }

        private void pintarCirculo(Graphics2D g2, double x, double y, double tamano, Color color) {
            int diametro = (int) (Math.min(getWidth(), getHeight()) * tamano);
            int posicionX = (int) (getWidth() * x);
            int posicionY = (int) (getHeight() * y);
            g2.setColor(color);
            g2.fillOval(posicionX, posicionY, diametro, diametro);
        }
    }

    private static final class CampoIconoPanel extends JPanel {

        private final JTextComponent campo;

        private CampoIconoPanel(JTextComponent campo, TipoIcono tipoIcono) {
            this.campo = campo;
            setOpaque(false);
            setLayout(new BorderLayout(10, 0));
            setBorder(new EmptyBorder(8, 12, 8, 14));
            add(new IconoCampo(tipoIcono, ICONO_CAMPO), BorderLayout.WEST);
            add(campo, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.setColor(BORDE_CAMPO);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class BotonPrimario extends JButton {

        private BotonPrimario(String texto) {
            super(texto);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(TemaVisual.fuente(Font.BOLD, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ButtonModel modelo = getModel();
            Color fondo = BOTON_PRIMARIO;
            if (modelo.isPressed()) {
                fondo = fondo.darker();
            } else if (modelo.isRollover()) {
                fondo = new Color(22, 97, 93);
            }

            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class IconoCabeceraUsuarios extends JComponent {

        private IconoCabeceraUsuarios() {
            setOpaque(false);
            setPreferredSize(new Dimension(58, 58));
            setMinimumSize(getPreferredSize());
            setMaximumSize(getPreferredSize());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BOTON_PRIMARIO);
            g2.fillRoundRect(0, 0, 58, 58, 18, 18);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(17, 16, 11, 11);
            g2.drawOval(31, 18, 9, 9);
            g2.drawArc(13, 26, 20, 15, 0, 180);
            g2.drawArc(28, 28, 16, 13, 0, 180);
            g2.drawLine(37, 14, 41, 14);
            g2.drawLine(39, 12, 39, 16);
            g2.dispose();
        }
    }

    private static final class IconoCampo extends JComponent {

        private final TipoIcono tipo;
        private final Color color;

        private IconoCampo(TipoIcono tipo, Color color) {
            this.tipo = tipo;
            this.color = color;
            setOpaque(false);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(16, 16);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (tipo) {
                case USUARIO -> pintarUsuario(g2);
                case CANDADO -> pintarCandado(g2);
                case INFO -> pintarInfo(g2);
                default -> {
                }
            }
            g2.dispose();
        }

        private void pintarUsuario(Graphics2D g2) {
            g2.drawOval(4, 2, 8, 8);
            g2.drawArc(2, 8, 12, 7, 0, 180);
        }

        private void pintarCandado(Graphics2D g2) {
            g2.drawRoundRect(3, 7, 10, 7, 2, 2);
            g2.drawArc(4, 2, 8, 9, 35, 110);
        }

        private void pintarInfo(Graphics2D g2) {
            g2.drawOval(1, 1, 14, 14);
            g2.drawLine(8, 6, 8, 10);
            g2.fillOval(7, 3, 2, 2);
        }
    }

    private static final class PanelCredenciales extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(FONDO_INFO);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.setColor(BORDE_INFO);
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                    1f, new float[]{4f, 4f}, 0f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
