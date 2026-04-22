package presentacion.componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import presentacion.estilo.TemaVisual;

/**
 * Panel base para tarjetas con esquinas redondeadas.
 */
public class PanelRedondeado extends JPanel {

    private final int radio;
    private Color colorBorde;
    private Color colorFondo;

    public PanelRedondeado(Color colorFondo, int radio) {
        this(colorFondo, radio, TemaVisual.BORDE);
    }

    public PanelRedondeado(Color colorFondo, int radio, Color colorBorde) {
        this.radio = radio;
        this.colorBorde = colorBorde;
        this.colorFondo = colorFondo;
        setOpaque(false);
    }

    public Color getColorFondo() { return colorFondo; }
    public void setColorFondo(Color c) { colorFondo = c; }
    public Color getColorBorde() { return colorBorde; }
    public void setColorBorde(Color c) { colorBorde = c; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 12));
        g2.fillRoundRect(2, 3, getWidth() - 3, getHeight() - 3, radio, radio);
        g2.setColor(colorFondo);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
        g2.setColor(colorBorde);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
        g2.dispose();
        super.paintComponent(g);
    }
}
