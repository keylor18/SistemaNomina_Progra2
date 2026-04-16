package presentacion.componentes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import presentacion.estilo.TemaVisual;

/**
 * Renderiza estados textuales con colores suaves.
 */
public class RenderizadorEstadoTabla extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String texto = value == null ? "" : value.toString();
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(TemaVisual.fuente(Font.BOLD, 12));
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        if (isSelected) {
            setBackground(new Color(225, 238, 235));
        } else {
            setBackground(Color.WHITE);
        }
        if ("Si".equalsIgnoreCase(texto) || "Generado".equalsIgnoreCase(texto) || "Enviado".equalsIgnoreCase(texto)) {
            setForeground(TemaVisual.EXITO);
        } else if ("No".equalsIgnoreCase(texto) || "Pendiente".equalsIgnoreCase(texto)) {
            setForeground(TemaVisual.ACENTO.darker());
        } else {
            setForeground(TemaVisual.TEXTO);
        }
        setText(texto);
        return this;
    }
}
