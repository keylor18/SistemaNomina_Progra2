package utilidades;

import excepciones.ValidacionException;
import org.junit.Test;

/**
 * Pruebas para la política de contraseñas.
 */
public class ValidacionesUtilTest {

    @Test
    public void debeAceptarContrasenaRobusta() throws Exception {
        ValidacionesUtil.validarContrasenaSegura("Z9!mR2@pLs");
    }

    @Test(expected = ValidacionException.class)
    public void debeRechazarContrasenaMuyCorta() throws Exception {
        ValidacionesUtil.validarContrasenaSegura("Ab1!cDef");
    }

    @Test(expected = ValidacionException.class)
    public void debeRechazarContrasenaSinCaracterEspecial() throws Exception {
        ValidacionesUtil.validarContrasenaSegura("Abcdef1234");
    }

    @Test(expected = ValidacionException.class)
    public void debeRechazarContrasenaConPalabraComun() throws Exception {
        ValidacionesUtil.validarContrasenaSegura("Clave!2026AZ");
    }
}
