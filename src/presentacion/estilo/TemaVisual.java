package presentacion.estilo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Define la identidad visual compartida de la capa de presentacion.
 */
public final class TemaVisual {

    // ---- Paleta clara (valores de referencia inamovibles) ----
    private static final Color L_FONDO_APP         = new Color(244, 241, 235);
    private static final Color L_SUPERFICIE        = new Color(255, 252, 247);
    private static final Color L_SUPERFICIE_SEC    = new Color(250, 246, 240);
    private static final Color L_PRIMARIO          = new Color(18, 93, 95);
    private static final Color L_PRIMARIO_OSC      = new Color(11, 55, 56);
    private static final Color L_ACENTO            = new Color(198, 142, 68);
    private static final Color L_TEXTO             = new Color(33, 43, 54);
    private static final Color L_TEXTO_SUAVE       = new Color(102, 112, 133);
    private static final Color L_BORDE             = new Color(219, 210, 197);
    private static final Color L_EXITO             = new Color(49, 122, 92);
    private static final Color L_PELIGRO           = new Color(176, 69, 69);
    private static final Color L_SOMBRA            = new Color(0, 0, 0, 25);

    // ---- Paleta oscura (valores de referencia inamovibles) ----
    private static final Color D_FONDO_APP         = new Color(22, 24, 30);
    private static final Color D_SUPERFICIE        = new Color(30, 33, 42);
    private static final Color D_SUPERFICIE_SEC    = new Color(38, 42, 52);
    private static final Color D_PRIMARIO          = new Color(48, 168, 172);
    private static final Color D_PRIMARIO_OSC      = new Color(30, 118, 122);
    private static final Color D_ACENTO            = new Color(208, 158, 80);
    private static final Color D_TEXTO             = new Color(215, 220, 228);
    private static final Color D_TEXTO_SUAVE       = new Color(128, 138, 155);
    private static final Color D_BORDE             = new Color(50, 55, 68);
    private static final Color D_EXITO             = new Color(68, 178, 112);
    private static final Color D_PELIGRO           = new Color(210, 88, 88);
    private static final Color D_SOMBRA            = new Color(0, 0, 0, 80);

    // ---- Campos activos (se modifican al cambiar el tema) ----
    public static Color FONDO_APP            = L_FONDO_APP;
    public static Color SUPERFICIE           = L_SUPERFICIE;
    public static Color SUPERFICIE_SECUNDARIA = L_SUPERFICIE_SEC;
    public static Color PRIMARIO             = L_PRIMARIO;
    public static Color PRIMARIO_OSCURO      = L_PRIMARIO_OSC;
    public static Color ACENTO               = L_ACENTO;
    public static Color TEXTO                = L_TEXTO;
    public static Color TEXTO_SUAVE          = L_TEXTO_SUAVE;
    public static Color BORDE                = L_BORDE;
    public static Color EXITO                = L_EXITO;
    public static Color PELIGRO              = L_PELIGRO;
    public static Color SOMBRA               = L_SOMBRA;
    public static Border BORDE_CAMPO = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(L_BORDE, 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12));

    private static boolean modoOscuro = false;
    private static final List<Runnable> temaListeners = new ArrayList<>();

    private TemaVisual() {
    }

    public static boolean isModoOscuro() {
        return modoOscuro;
    }

    public static void addTemaListener(Runnable listener) {
        temaListeners.add(listener);
    }

    /**
     * Cambia la paleta activa y retorna un mapa viejo->nuevo para que el
     * llamador aplique la transicion en el arbol de componentes.
     */
    public static Map<Color, Color> cambiarPaleta(boolean oscuro) {
        Map<Color, Color> mapa = new LinkedHashMap<>();
        mapa.put(FONDO_APP,             oscuro ? D_FONDO_APP     : L_FONDO_APP);
        mapa.put(SUPERFICIE,            oscuro ? D_SUPERFICIE    : L_SUPERFICIE);
        mapa.put(SUPERFICIE_SECUNDARIA, oscuro ? D_SUPERFICIE_SEC : L_SUPERFICIE_SEC);
        mapa.put(PRIMARIO,              oscuro ? D_PRIMARIO      : L_PRIMARIO);
        mapa.put(PRIMARIO_OSCURO,       oscuro ? D_PRIMARIO_OSC  : L_PRIMARIO_OSC);
        mapa.put(ACENTO,                oscuro ? D_ACENTO        : L_ACENTO);
        mapa.put(TEXTO,                 oscuro ? D_TEXTO         : L_TEXTO);
        mapa.put(TEXTO_SUAVE,           oscuro ? D_TEXTO_SUAVE   : L_TEXTO_SUAVE);
        mapa.put(BORDE,                 oscuro ? D_BORDE         : L_BORDE);
        mapa.put(EXITO,                 oscuro ? D_EXITO         : L_EXITO);
        mapa.put(PELIGRO,               oscuro ? D_PELIGRO       : L_PELIGRO);
        mapa.put(SOMBRA,                oscuro ? D_SOMBRA        : L_SOMBRA);

        modoOscuro              = oscuro;
        FONDO_APP               = oscuro ? D_FONDO_APP     : L_FONDO_APP;
        SUPERFICIE              = oscuro ? D_SUPERFICIE    : L_SUPERFICIE;
        SUPERFICIE_SECUNDARIA   = oscuro ? D_SUPERFICIE_SEC : L_SUPERFICIE_SEC;
        PRIMARIO                = oscuro ? D_PRIMARIO      : L_PRIMARIO;
        PRIMARIO_OSCURO         = oscuro ? D_PRIMARIO_OSC  : L_PRIMARIO_OSC;
        ACENTO                  = oscuro ? D_ACENTO        : L_ACENTO;
        TEXTO                   = oscuro ? D_TEXTO         : L_TEXTO;
        TEXTO_SUAVE             = oscuro ? D_TEXTO_SUAVE   : L_TEXTO_SUAVE;
        BORDE                   = oscuro ? D_BORDE         : L_BORDE;
        EXITO                   = oscuro ? D_EXITO         : L_EXITO;
        PELIGRO                 = oscuro ? D_PELIGRO       : L_PELIGRO;
        SOMBRA                  = oscuro ? D_SOMBRA        : L_SOMBRA;
        BORDE_CAMPO             = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12));

        UIManager.put("control",               new ColorUIResource(FONDO_APP));
        UIManager.put("nimbusBase",            new ColorUIResource(PRIMARIO_OSCURO));
        UIManager.put("nimbusBlueGrey",        new ColorUIResource(
                oscuro ? new Color(55, 60, 75) : new Color(222, 216, 206)));
        UIManager.put("nimbusFocus",           new ColorUIResource(ACENTO));
        UIManager.put("TabbedPane.selected",   new ColorUIResource(SUPERFICIE));
        UIManager.put("Table.gridColor",       new ColorUIResource(BORDE));
        UIManager.put("Table.selectionBackground", new ColorUIResource(
                oscuro ? new Color(48, 82, 78) : new Color(221, 234, 231)));
        UIManager.put("Table.selectionForeground", new ColorUIResource(TEXTO));
        UIManager.put("ScrollBar.thumb",       new ColorUIResource(
                oscuro ? new Color(68, 74, 90) : new Color(180, 170, 157)));
        UIManager.put("OptionPane.background", new ColorUIResource(SUPERFICIE));
        UIManager.put("Panel.background",      new ColorUIResource(FONDO_APP));

        temaListeners.forEach(Runnable::run);
        return mapa;
    }

    /**
     * Instala una base visual consistente para la aplicacion.
     */
    public static void instalarTema() {
        instalarNimbusSiExiste();
        UIManager.put("control", new ColorUIResource(FONDO_APP));
        UIManager.put("nimbusBase", new ColorUIResource(PRIMARIO_OSCURO));
        UIManager.put("nimbusBlueGrey", new ColorUIResource(new Color(222, 216, 206)));
        UIManager.put("nimbusFocus", new ColorUIResource(ACENTO));
        UIManager.put("TabbedPane.selected", new ColorUIResource(SUPERFICIE));
        UIManager.put("Table.gridColor", new ColorUIResource(BORDE));
        UIManager.put("Table.selectionBackground", new ColorUIResource(new Color(221, 234, 231)));
        UIManager.put("Table.selectionForeground", new ColorUIResource(TEXTO));
        UIManager.put("ScrollBar.thumb", new ColorUIResource(new Color(180, 170, 157)));
        UIManager.put("OptionPane.background", new ColorUIResource(SUPERFICIE));
        UIManager.put("Panel.background", new ColorUIResource(FONDO_APP));
    }

    /**
     * Crea una fuente de interfaz uniforme.
     */
    public static Font fuente(int estilo, float tamano) {
        return new Font("Segoe UI", estilo, Math.round(tamano));
    }

    public static void estilizarBotonPrimario(JButton boton) {
        estilizarBoton(boton, PRIMARIO, Color.WHITE);
    }

    public static void estilizarBotonSecundario(JButton boton) {
        estilizarBoton(boton, SUPERFICIE_SECUNDARIA, TEXTO);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
    }

    public static void estilizarBotonPeligro(JButton boton) {
        estilizarBoton(boton, PELIGRO, Color.WHITE);
    }

    public static void estilizarCampo(JComponent campo) {
        campo.setFont(fuente(Font.PLAIN, 14));
        campo.setBackground(Color.WHITE);
        campo.setForeground(TEXTO);
        campo.setBorder(BORDE_CAMPO);
        campo.setPreferredSize(new Dimension(220, 42));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARIO, 2, true),
                        BorderFactory.createEmptyBorder(9, 11, 9, 11)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BORDE_CAMPO);
            }
        });
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(fuente(Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXTO);
        combo.setBorder(BORDE_CAMPO);
        combo.setPreferredSize(new Dimension(220, 42));
    }

    public static void estilizarSpinner(JSpinner spinner) {
        spinner.setFont(fuente(Font.PLAIN, 14));
        spinner.setBorder(BORDE_CAMPO);
        spinner.setPreferredSize(new Dimension(220, 42));
        if (spinner.getEditor() instanceof JSpinner.DefaultEditor editor) {
            JTextField campo = editor.getTextField();
            campo.setBackground(Color.WHITE);
            campo.setForeground(TEXTO);
            campo.setBorder(BorderFactory.createEmptyBorder());
            campo.setHorizontalAlignment(SwingConstants.LEFT);
            campo.setFont(fuente(Font.PLAIN, 14));
        }
    }

    public static void estilizarCheck(JCheckBox check) {
        check.setOpaque(false);
        check.setForeground(TEXTO);
        check.setFont(fuente(Font.PLAIN, 13));
    }

    public static void estilizarScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(BORDE, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setOpaque(false);
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(fuente(Font.PLAIN, 13));
        tabla.setRowHeight(34);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(225, 238, 235));
        tabla.setSelectionForeground(TEXTO);
        tabla.setGridColor(BORDE);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setBackground(TemaVisual.SUPERFICIE_SECUNDARIA);
                setForeground(TemaVisual.TEXTO);
                setFont(fuente(Font.BOLD, 13));
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, TemaVisual.BORDE));
                setHorizontalAlignment(CENTER);
                return this;
            }
        });
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.setDefaultRenderer(Object.class, new RendererBaseTabla());
        tabla.setDefaultRenderer(Number.class, centrado);
    }

    public static void estilizarTabs(JTabbedPane tabs) {
        tabs.setFont(fuente(Font.BOLD, 14));
        tabs.setBackground(FONDO_APP);
        tabs.setForeground(TEXTO);
        tabs.setBorder(BorderFactory.createEmptyBorder());
    }

    public static JLabel crearTituloSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente(Font.BOLD, 24));
        label.setForeground(TEXTO);
        return label;
    }

    public static JLabel crearSubtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente(Font.PLAIN, 13));
        label.setForeground(TEXTO_SUAVE);
        return label;
    }

    public static JLabel crearEtiquetaCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente(Font.BOLD, 12));
        label.setForeground(TEXTO_SUAVE);
        return label;
    }

    public static Border crearPaddingTarjeta() {
        return BorderFactory.createEmptyBorder(20, 22, 20, 22);
    }

    public static String colorHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private static void instalarNimbusSiExiste() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (Exception ex) {
            // Si Nimbus falla, Swing usa el look and feel por defecto.
        }
    }

    private static void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setFont(fuente(Font.BOLD, 13));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        boton.setPreferredSize(new Dimension(160, 42));
        boton.putClientProperty("normalBg", fondo);
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Color bg = (Color) boton.getClientProperty("normalBg");
                boton.setBackground(bg != null ? bg.darker() : boton.getBackground().darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Color bg = (Color) boton.getClientProperty("normalBg");
                if (bg != null) boton.setBackground(bg);
            }
        });
    }

    private static final class RendererBaseTabla extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setFont(TemaVisual.fuente(Font.PLAIN, 13));
            setForeground(isSelected ? PRIMARIO_OSCURO : TEXTO);
            setBackground(isSelected
                    ? (modoOscuro ? new Color(48, 82, 78) : new Color(225, 238, 235))
                    : (modoOscuro ? SUPERFICIE : Color.WHITE));
            return this;
        }
    }
}
