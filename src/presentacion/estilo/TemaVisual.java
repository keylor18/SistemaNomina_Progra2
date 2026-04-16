package presentacion.estilo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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

    public static final Color FONDO_APP = new Color(244, 241, 235);
    public static final Color SUPERFICIE = new Color(255, 252, 247);
    public static final Color SUPERFICIE_SECUNDARIA = new Color(250, 246, 240);
    public static final Color PRIMARIO = new Color(18, 93, 95);
    public static final Color PRIMARIO_OSCURO = new Color(11, 55, 56);
    public static final Color ACENTO = new Color(198, 142, 68);
    public static final Color TEXTO = new Color(33, 43, 54);
    public static final Color TEXTO_SUAVE = new Color(102, 112, 133);
    public static final Color BORDE = new Color(219, 210, 197);
    public static final Color EXITO = new Color(49, 122, 92);
    public static final Color PELIGRO = new Color(176, 69, 69);
    public static final Color SOMBRA = new Color(0, 0, 0, 25);
    public static final Border BORDE_CAMPO = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12));

    private TemaVisual() {
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
     *
     * @param estilo peso de la fuente
     * @param tamano tamano en puntos
     * @return fuente configurada
     */
    public static Font fuente(int estilo, float tamano) {
        return new Font("Segoe UI", estilo, Math.round(tamano));
    }

    /**
     * Configura un boton principal.
     *
     * @param boton boton a estilizar
     */
    public static void estilizarBotonPrimario(JButton boton) {
        estilizarBoton(boton, PRIMARIO, Color.WHITE);
    }

    /**
     * Configura un boton secundario.
     *
     * @param boton boton a estilizar
     */
    public static void estilizarBotonSecundario(JButton boton) {
        estilizarBoton(boton, SUPERFICIE_SECUNDARIA, TEXTO);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
    }

    /**
     * Configura un boton de accion critica.
     *
     * @param boton boton a estilizar
     */
    public static void estilizarBotonPeligro(JButton boton) {
        estilizarBoton(boton, PELIGRO, Color.WHITE);
    }

    /**
     * Aplica estilo base a campos de texto.
     *
     * @param campo componente a estilizar
     */
    public static void estilizarCampo(JComponent campo) {
        campo.setFont(fuente(Font.PLAIN, 14));
        campo.setBackground(Color.WHITE);
        campo.setForeground(TEXTO);
        campo.setBorder(BORDE_CAMPO);
        campo.setPreferredSize(new Dimension(220, 42));
    }

    /**
     * Aplica estilo a un combo box.
     *
     * @param combo componente a estilizar
     */
    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(fuente(Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXTO);
        combo.setBorder(BORDE_CAMPO);
        combo.setPreferredSize(new Dimension(220, 42));
    }

    /**
     * Aplica estilo a un spinner y su editor interno.
     *
     * @param spinner componente a estilizar
     */
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

    /**
     * Aplica estilo a una casilla de verificacion.
     *
     * @param check componente a estilizar
     */
    public static void estilizarCheck(JCheckBox check) {
        check.setOpaque(false);
        check.setForeground(TEXTO);
        check.setFont(fuente(Font.PLAIN, 13));
    }

    /**
     * Aplica estilo a un scroll pane.
     *
     * @param scroll scroll a estilizar
     */
    public static void estilizarScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(BORDE, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setOpaque(false);
    }

    /**
     * Aplica estilo a una tabla de datos.
     *
     * @param tabla tabla a estilizar
     */
    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(fuente(Font.PLAIN, 13));
        tabla.setRowHeight(34);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(225, 238, 235));
        tabla.setSelectionForeground(TEXTO);
        tabla.setGridColor(BORDE);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setFont(fuente(Font.BOLD, 13));
        tabla.getTableHeader().setBackground(SUPERFICIE_SECUNDARIA);
        tabla.getTableHeader().setForeground(TEXTO);
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.setDefaultRenderer(Object.class, new RendererBaseTabla());
        tabla.setDefaultRenderer(Number.class, centrado);
    }

    /**
     * Aplica estilo a un selector de pestanas.
     *
     * @param tabs componente a estilizar
     */
    public static void estilizarTabs(JTabbedPane tabs) {
        tabs.setFont(fuente(Font.BOLD, 14));
        tabs.setBackground(FONDO_APP);
        tabs.setForeground(TEXTO);
        tabs.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Crea una etiqueta de seccion principal.
     *
     * @param texto contenido
     * @return etiqueta configurada
     */
    public static JLabel crearTituloSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente(Font.BOLD, 24));
        label.setForeground(TEXTO);
        return label;
    }

    /**
     * Crea una etiqueta descriptiva secundaria.
     *
     * @param texto contenido
     * @return etiqueta configurada
     */
    public static JLabel crearSubtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente(Font.PLAIN, 13));
        label.setForeground(TEXTO_SUAVE);
        return label;
    }

    /**
     * Crea una etiqueta para encabezados pequenos.
     *
     * @param texto contenido
     * @return etiqueta configurada
     */
    public static JLabel crearEtiquetaCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente(Font.BOLD, 12));
        label.setForeground(TEXTO_SUAVE);
        return label;
    }

    /**
     * Devuelve un borde interno estandar para tarjetas.
     *
     * @return borde reutilizable
     */
    public static Border crearPaddingTarjeta() {
        return BorderFactory.createEmptyBorder(20, 22, 20, 22);
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
    }

    private static final class RendererBaseTabla extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setFont(TemaVisual.fuente(Font.PLAIN, 13));
            setForeground(isSelected ? TEXTO : TEXTO);
            setBackground(isSelected ? new Color(225, 238, 235) : Color.WHITE);
            return this;
        }
    }
}
