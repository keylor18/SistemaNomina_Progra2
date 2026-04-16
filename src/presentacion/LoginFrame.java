package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Pantalla de autenticacion del sistema.
 */
public class LoginFrame extends JFrame {

    private final JTextField txtUsuario;
    private final JPasswordField txtContrasena;
    private final JButton btnIngresar;
    private final JLabel lblEstado;

    public LoginFrame() {
        setTitle("Sistema de Nomina Empresarial - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(460, 320));
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contenedor = new JPanel(new BorderLayout(0, 12));
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Sistema de Nomina Empresarial", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        contenedor.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(new JLabel("Usuario:"), gbc);

        txtUsuario = new JTextField("admin", 20);
        gbc.gridx = 1;
        formulario.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formulario.add(new JLabel("Contrasena:"), gbc);

        txtContrasena = new JPasswordField("Admin123", 20);
        gbc.gridx = 1;
        formulario.add(txtContrasena, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel ayuda = new JLabel("Credenciales iniciales: admin / Admin123", SwingConstants.CENTER);
        ayuda.setForeground(new Color(70, 70, 70));
        formulario.add(ayuda, gbc);

        gbc.gridy = 3;
        btnIngresar = new JButton("Ingresar");
        formulario.add(btnIngresar, gbc);

        gbc.gridy = 4;
        lblEstado = new JLabel(" ", SwingConstants.CENTER);
        lblEstado.setForeground(new Color(180, 0, 0));
        formulario.add(lblEstado, gbc);

        contenedor.add(formulario, BorderLayout.CENTER);
        setContentPane(contenedor);
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
}
