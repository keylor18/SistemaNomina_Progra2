package utilidades;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Pruebas de conversion de montos en interfaz.
 */
public class FormatoUtilTest {

    @Test
    public void debeParsearMontoSinSeparadores() {
        assertEquals(450000.00, FormatoUtil.parsearMonto("450000"), 0.001);
    }

    @Test
    public void debeParsearMontoConComaDecimal() {
        assertEquals(450000.50, FormatoUtil.parsearMonto("450000,50"), 0.001);
    }

    @Test
    public void debeParsearMontoConFormatoLocalCompleto() {
        assertEquals(450000.50, FormatoUtil.parsearMonto("\u20A1450.000,50"), 0.001);
    }

    @Test
    public void debeParsearMontoConSimboloCorruptoPorCompatibilidad() {
        assertEquals(450000.50, FormatoUtil.parsearMonto("â‚¡450.000,50"), 0.001);
    }
}
