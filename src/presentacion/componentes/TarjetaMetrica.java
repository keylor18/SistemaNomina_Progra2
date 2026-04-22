package presentacion.componentes;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import presentacion.estilo.TemaVisual;

/**
 * Tarjeta simple para mostrar indicadores resumen.
 */
public class TarjetaMetrica extends PanelRedondeado {

    private final JLabel lblTitulo;
    private final JLabel lblValor;
    private final JLabel lblDetalle;

    public TarjetaMetrica(String titulo, String valor, String detalle) {
        super(TemaVisual.SUPERFICIE, 24);
        setLayout(new BorderLayout());
        setBorder(TemaVisual.crearPaddingTarjeta());

        lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(TemaVisual.fuente(Font.BOLD, 12));
        lblTitulo.setForeground(TemaVisual.TEXTO_SUAVE);

        lblValor = new JLabel(valor);
        lblValor.setFont(TemaVisual.fuente(Font.BOLD, 28));
        lblValor.setForeground(TemaVisual.TEXTO);

        lblDetalle = new JLabel(detalle);
        lblDetalle.setFont(TemaVisual.fuente(Font.PLAIN, 12));
        lblDetalle.setForeground(TemaVisual.TEXTO_SUAVE);

        javax.swing.JPanel contenido = new javax.swing.JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.add(lblTitulo);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(lblValor);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(lblDetalle);
        add(contenido, BorderLayout.CENTER);
    }

    public void actualizar(String valor, String detalle) {
        lblValor.setText(valor);
        lblDetalle.setText(detalle);
    }
}
