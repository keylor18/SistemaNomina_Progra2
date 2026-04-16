package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import presentacion.componentes.PanelGradiente;
import presentacion.componentes.PanelRedondeado;
import presentacion.estilo.TemaVisual;

/**
 * Pantalla de autenticacion del sistema.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblEstado;

    public LoginFrame() {
        setTitle("SistemaNomina_Progra2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1040, 640));
        setMinimumSize(new Dimension(980, 600));
        setLocationRelativeTo(null);
        setResizable(true);

        PanelGradiente fondo = new PanelGradiente(new Color(12, 48, 50), new Color(31, 99, 101));
        fondo.setLayout(new GridBagLayout());
        fondo.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        PanelRedondeado tarjetaPrincipal = new PanelRedondeado(TemaVisual.SUPERFICIE, 30);
        tarjetaPrincipal.setLayout(new GridLayout(1, 2));
        tarjetaPrincipal.setPreferredSize(new Dimension(920, 520));

        tarjetaPrincipal.add(crearPanelMarca());
        tarjetaPrincipal.add(crearPanelAcceso());

        fondo.add(tarjetaPrincipal, new GridBagConstraints());
        setContentPane(fondo);
        getRootPane().setDefaultButton(btnIngresar);
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
        lblEstado.setForeground(color);
        lblEstado.setText(mensaje);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private JPanel crearPanelMarca() {
        PanelGradiente panel = new PanelGradiente(TemaVisual.PRIMARIO, TemaVisual.PRIMARIO_OSCURO);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(42, 42, 42, 42));

        JLabel marca = new JLabel("SistemaNomina_Progra2");
        marca.setFont(TemaVisual.fuente(Font.BOLD, 13));
        marca.setForeground(new Color(227, 213, 191));

        JLabel titulo = new JLabel("<html>Gestion de planilla<br>clara, robusta y lista<br>para operar</html>");
        titulo.setFont(TemaVisual.fuente(Font.BOLD, 33));
        titulo.setForeground(Color.WHITE);

        JLabel descripcion = new JLabel("<html>Administre colaboradores, calcule nominas bajo normativa costarricense, "
                + "genere comprobantes PDF y automatice su envio desde una sola plataforma.</html>");
        descripcion.setFont(TemaVisual.fuente(Font.PLAIN, 15));
        descripcion.setForeground(new Color(221, 232, 228));

        panel.add(marca);
        panel.add(Box.createVerticalStrut(24));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(18));
        panel.add(descripcion);
        panel.add(Box.createVerticalStrut(28));
        panel.add(crearBullet("Arquitectura por capas con separacion estricta."));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearBullet("Persistencia local en archivos y trazabilidad de errores."));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearBullet("Reportes PDF y correo automatico integrados."));
        panel.add(Box.createVerticalGlue());

        PanelRedondeado sello = new PanelRedondeado(new Color(255, 255, 255, 30), 22, new Color(255, 255, 255, 45));
        sello.setLayout(new BorderLayout());
        sello.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        JLabel pie = new JLabel("<html><b>Base normativa:</b> Costa Rica 2026<br>CCSS, IVM, renta salarial y aportes patronales.</html>");
        pie.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        pie.setForeground(Color.WHITE);
        sello.add(pie, BorderLayout.CENTER);
        panel.add(sello);
        return panel;
    }

    private JPanel crearPanelAcceso() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(42, 44, 42, 44));

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Acceso al sistema");
        titulo.setFont(TemaVisual.fuente(Font.BOLD, 28));
        titulo.setForeground(TemaVisual.TEXTO);

        JLabel subtitulo = new JLabel("<html>Ingrese sus credenciales para acceder al modulo administrativo "
                + "de empleados, planilla y reportes.</html>");
        subtitulo.setFont(TemaVisual.fuente(Font.PLAIN, 14));
        subtitulo.setForeground(TemaVisual.TEXTO_SUAVE);

        txtUsuario = new JTextField("admin");
        txtContrasena = new JPasswordField("Admin123");
        TemaVisual.estilizarCampo(txtUsuario);
        TemaVisual.estilizarCampo(txtContrasena);

        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(subtitulo);
        contenido.add(Box.createVerticalStrut(28));
        contenido.add(crearBloqueCampo("Usuario", txtUsuario));
        contenido.add(Box.createVerticalStrut(16));
        contenido.add(crearBloqueCampo("Contrasena", txtContrasena));
        contenido.add(Box.createVerticalStrut(18));

        PanelRedondeado credenciales = new PanelRedondeado(TemaVisual.SUPERFICIE_SECUNDARIA, 22);
        credenciales.setLayout(new BorderLayout());
        credenciales.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JLabel ayuda = new JLabel("<html><b>Credenciales iniciales</b><br>Usuario: admin<br>Contrasena: Admin123</html>");
        ayuda.setForeground(TemaVisual.TEXTO);
        ayuda.setFont(TemaVisual.fuente(Font.PLAIN, 13));
        credenciales.add(ayuda, BorderLayout.CENTER);
        contenido.add(credenciales);
        contenido.add(Box.createVerticalStrut(18));

        btnIngresar = new JButton("Ingresar al panel");
        TemaVisual.estilizarBotonPrimario(btnIngresar);
        btnIngresar.setAlignmentX(LEFT_ALIGNMENT);
        contenido.add(btnIngresar);
        contenido.add(Box.createVerticalStrut(14));

        lblEstado = new JLabel(" ");
        lblEstado.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        lblEstado.setForeground(TemaVisual.PELIGRO);
        contenido.add(lblEstado);
        contenido.add(Box.createVerticalGlue());

        JPanel franja = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        franja.setOpaque(false);
        JLabel seguridad = new JLabel("Acceso restringido a usuarios autorizados");
        seguridad.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        seguridad.setForeground(TemaVisual.TEXTO_SUAVE);
        franja.add(seguridad);
        contenido.add(franja);

        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearBloqueCampo(String etiqueta, javax.swing.JComponent campo) {
        JPanel bloque = new JPanel();
        bloque.setOpaque(false);
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
        JLabel label = TemaVisual.crearEtiquetaCampo(etiqueta);
        bloque.add(label);
        bloque.add(Box.createVerticalStrut(8));
        bloque.add(campo);
        return bloque;
    }

    private JLabel crearBullet(String texto) {
        JLabel label = new JLabel("<html><span style='color:#E3D5BF;font-size:12px;'>&#9679;</span> " + texto + "</html>");
        label.setFont(TemaVisual.fuente(Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        return label;
    }
}
