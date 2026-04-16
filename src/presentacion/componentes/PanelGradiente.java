package presentacion.componentes;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Panel con fondo en degradado para encabezados y areas destacadas.
 */
public class PanelGradiente extends JPanel {

    private final Color colorSuperior;
    private final Color colorInferior;

    public PanelGradiente(Color colorSuperior, Color colorInferior) {
        this.colorSuperior = colorSuperior;
        this.colorInferior = colorInferior;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setPaint(new GradientPaint(0, 0, colorSuperior, 0, getHeight(), colorInferior));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
