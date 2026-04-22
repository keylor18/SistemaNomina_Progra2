package presentacion;

import entidades.Usuario;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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
    private JButton btnTema;

    public MainFrame(Usuario usuario) {
        setTitle("Sistema Nómina");
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
        PanelGradiente encabezado = new PanelGradiente(new Color(18, 93, 95), new Color(11, 55, 56));
        encabezado.setLayout(new BorderLayout(16, 16));
        encabezado.setBorder(BorderFactory.createEmptyBorder(26, 28, 26, 28));

        JPanel bloqueTitulo = new JPanel();
        bloqueTitulo.setOpaque(false);
        bloqueTitulo.setLayout(new BoxLayout(bloqueTitulo, BoxLayout.Y_AXIS));

        JLabel nombre = new JLabel("Sistema Nómina");
        nombre.setFont(TemaVisual.fuente(Font.BOLD, 28));
        nombre.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Operación administrativa, cálculo de planilla, reportes y trazabilidad.");
        subtitulo.setFont(TemaVisual.fuente(Font.PLAIN, 14));
        subtitulo.setForeground(new Color(222, 234, 231));

        bloqueTitulo.add(nombre);
        bloqueTitulo.add(Box.createVerticalStrut(8));
        bloqueTitulo.add(subtitulo);

        btnTema = new BotonTema();
        btnTema.addActionListener(e -> toggleTema());

        JPanel bloqueUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        bloqueUsuario.setOpaque(false);
        bloqueUsuario.add(crearBloqueTema());
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
        tabs.addTab("Nómina y reportes", nominaPanel);
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

    private Component crearBloqueTema() {
        JPanel bloqueTema = new JPanel();
        bloqueTema.setOpaque(false);
        bloqueTema.setLayout(new BoxLayout(bloqueTema, BoxLayout.Y_AXIS));

        JLabel etiqueta = new JLabel("Tema visual");
        etiqueta.setFont(TemaVisual.fuente(Font.BOLD, 11));
        etiqueta.setForeground(new Color(230, 237, 234));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnTema.setAlignmentX(Component.LEFT_ALIGNMENT);

        bloqueTema.add(etiqueta);
        bloqueTema.add(Box.createVerticalStrut(6));
        bloqueTema.add(btnTema);
        return bloqueTema;
    }

    private Component crearTarjetaInfo(String titulo, String valor) {
        PanelRedondeado tarjeta = new PanelRedondeado(new Color(255, 255, 255, 30), 22,
                new Color(255, 255, 255, 40));
        tarjeta.setLayout(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(TemaVisual.fuente(Font.BOLD, 11));
        lblTitulo.setForeground(new Color(230, 237, 234));
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(TemaVisual.fuente(Font.BOLD, 13));
        lblValor.setForeground(Color.WHITE);
        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(4));
        contenido.add(lblValor);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private void toggleTema() {
        boolean nuevoModo = !TemaVisual.isModoOscuro();
        Map<Color, Color> mapa = TemaVisual.cambiarPaleta(nuevoModo);
        reaplicarArbol(this, mapa);
        btnTema.repaint();
        repaint();
    }

    private void reaplicarArbol(Component c, Map<Color, Color> mapa) {
        if (c instanceof PanelRedondeado pr) {
            Color nf = mapa.get(pr.getColorFondo());
            if (nf != null) {
                pr.setColorFondo(nf);
            }
            Color nb = mapa.get(pr.getColorBorde());
            if (nb != null) {
                pr.setColorBorde(nb);
            }
        } else if (c instanceof JPanel p && p.isOpaque()) {
            Color nb = mapa.get(p.getBackground());
            if (nb != null) {
                p.setBackground(nb);
            }
        }

        if (c instanceof JLabel l) {
            Color nf = mapa.get(l.getForeground());
            if (nf != null) {
                l.setForeground(nf);
            }
        }

        if (c instanceof JButton b && b != btnTema) {
            Color nbg = mapa.get(b.getBackground());
            if (nbg != null) {
                b.setBackground(nbg);
                b.putClientProperty("normalBg", nbg);
            }
            Color nfg = mapa.get(b.getForeground());
            if (nfg != null) {
                b.setForeground(nfg);
            }
            if (b.getBorder() instanceof javax.swing.border.CompoundBorder cb2
                    && cb2.getOutsideBorder() instanceof javax.swing.border.LineBorder) {
                b.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaVisual.BORDE, 1, true),
                        cb2.getInsideBorder()));
            }
        }

        if (c instanceof JCheckBox cb) {
            Color nf = mapa.get(cb.getForeground());
            if (nf != null) {
                cb.setForeground(nf);
            }
        }

        if (c instanceof JEditorPane ep) {
            ep.setBackground(TemaVisual.isModoOscuro() ? TemaVisual.SUPERFICIE : Color.WHITE);
        }

        if (c instanceof JScrollPane sp) {
            Color vbg = sp.getViewport().getBackground();
            if (Color.WHITE.equals(vbg) || mapa.containsKey(vbg)) {
                sp.getViewport().setBackground(
                        TemaVisual.isModoOscuro() ? TemaVisual.SUPERFICIE : Color.WHITE);
            }
            if (sp.getBorder() instanceof javax.swing.border.LineBorder) {
                sp.setBorder(BorderFactory.createLineBorder(TemaVisual.BORDE, 1, true));
            }
        }

        if (c instanceof JTable t) {
            t.setBackground(TemaVisual.isModoOscuro() ? TemaVisual.SUPERFICIE : Color.WHITE);
            t.setForeground(TemaVisual.TEXTO);
            t.setGridColor(TemaVisual.BORDE);
            t.setSelectionBackground(TemaVisual.isModoOscuro()
                    ? new Color(48, 82, 78) : new Color(225, 238, 235));
            t.setSelectionForeground(TemaVisual.TEXTO);
            t.getTableHeader().setBackground(TemaVisual.SUPERFICIE_SECUNDARIA);
            t.getTableHeader().setForeground(TemaVisual.TEXTO);
            t.getTableHeader().repaint();
        }

        if (c instanceof JComboBox<?> cb) {
            cb.setBackground(TemaVisual.isModoOscuro() ? TemaVisual.SUPERFICIE_SECUNDARIA : Color.WHITE);
            cb.setForeground(Color.BLACK);
        }
        if (c instanceof JSpinner sp) {
            if (sp.getEditor() instanceof JSpinner.DefaultEditor ed) {
                ed.getTextField().setBackground(TemaVisual.isModoOscuro()
                        ? TemaVisual.SUPERFICIE_SECUNDARIA : Color.WHITE);
                ed.getTextField().setForeground(Color.BLACK);
            }
        }

        if (c instanceof JTabbedPane tp) {
            tp.setBackground(TemaVisual.FONDO_APP);
            tp.setForeground(TemaVisual.TEXTO);
        }

        c.repaint();

        if (c instanceof Container cont) {
            for (Component child : cont.getComponents()) {
                reaplicarArbol(child, mapa);
            }
        }
    }

    private static final class BotonTema extends JButton {

        private static final int ANCHO = 188;
        private static final int ALTO = 46;

        private BotonTema() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("Cambiar entre modo claro y modo oscuro");
            Dimension tamano = new Dimension(ANCHO, ALTO);
            setPreferredSize(tamano);
            setMinimumSize(tamano);
            setMaximumSize(tamano);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int margen = 3;
            int anchoSegmento = (ancho - (margen * 2) - 2) / 2;
            int altoSegmento = alto - (margen * 2);
            boolean oscuro = TemaVisual.isModoOscuro();

            Color pista = getModel().isRollover()
                    ? new Color(255, 255, 255, 58)
                    : new Color(255, 255, 255, 38);
            g2.setColor(pista);
            g2.fillRoundRect(0, 0, ancho - 1, alto - 1, 22, 22);
            g2.setColor(new Color(255, 255, 255, 95));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, ancho - 1, alto - 1, 22, 22);

            int xActivo = oscuro ? margen + anchoSegmento + 2 : margen;
            Color fondoActivo = oscuro ? new Color(16, 27, 43, 232) : new Color(244, 195, 82, 245);
            Color bordeActivo = oscuro ? new Color(114, 154, 214, 180) : new Color(255, 235, 181, 220);
            g2.setColor(fondoActivo);
            g2.fillRoundRect(xActivo, margen, anchoSegmento, altoSegmento, 18, 18);
            g2.setColor(bordeActivo);
            g2.drawRoundRect(xActivo, margen, anchoSegmento, altoSegmento, 18, 18);

            pintarOpcion(g2, margen, margen, anchoSegmento, altoSegmento,
                    "Claro", !oscuro, true, oscuro ? pista : fondoActivo);
            pintarOpcion(g2, margen + anchoSegmento + 2, margen, anchoSegmento, altoSegmento,
                    "Oscuro", oscuro, false, oscuro ? fondoActivo : pista);

            g2.dispose();
        }

        private void pintarOpcion(Graphics2D g2, int x, int y, int ancho, int alto,
                String texto, boolean activa, boolean sol, Color colorMascara) {
            Color colorTexto = activa
                    ? (sol ? new Color(91, 58, 0) : Color.WHITE)
                    : new Color(255, 255, 255, 216);
            Color colorIcono = activa
                    ? (sol ? new Color(126, 79, 0) : new Color(236, 243, 255))
                    : new Color(255, 255, 255, 216);

            g2.setFont(TemaVisual.fuente(Font.BOLD, 12));
            int icono = 14;
            int separacion = 7;
            int anchoTexto = g2.getFontMetrics().stringWidth(texto);
            int anchoGrupo = icono + separacion + anchoTexto;
            int inicioX = x + (ancho - anchoGrupo) / 2;
            int centroY = y + (alto / 2);

            if (sol) {
                pintarSol(g2, inicioX + (icono / 2), centroY, colorIcono);
            } else {
                pintarLuna(g2, inicioX + (icono / 2), centroY, colorIcono, colorMascara);
            }

            g2.setColor(colorTexto);
            int lineaBase = centroY + (g2.getFontMetrics().getAscent() / 2) - 2;
            g2.drawString(texto, inicioX + icono + separacion, lineaBase);
        }

        private void pintarSol(Graphics2D g2, int cx, int cy, Color color) {
            Graphics2D copia = (Graphics2D) g2.create();
            copia.setColor(color);
            copia.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            copia.drawOval(cx - 4, cy - 4, 8, 8);
            copia.drawLine(cx, cy - 8, cx, cy - 11);
            copia.drawLine(cx, cy + 8, cx, cy + 11);
            copia.drawLine(cx - 8, cy, cx - 11, cy);
            copia.drawLine(cx + 8, cy, cx + 11, cy);
            copia.drawLine(cx - 6, cy - 6, cx - 8, cy - 8);
            copia.drawLine(cx + 6, cy - 6, cx + 8, cy - 8);
            copia.drawLine(cx - 6, cy + 6, cx - 8, cy + 8);
            copia.drawLine(cx + 6, cy + 6, cx + 8, cy + 8);
            copia.dispose();
        }

        private void pintarLuna(Graphics2D g2, int cx, int cy, Color color, Color mascara) {
            Graphics2D copia = (Graphics2D) g2.create();
            copia.setColor(color);
            copia.fillOval(cx - 5, cy - 5, 10, 10);
            copia.setColor(mascara);
            copia.fillOval(cx - 1, cy - 6, 10, 10);
            copia.dispose();
        }
    }
}
