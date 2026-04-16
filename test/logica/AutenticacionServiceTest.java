package logica;

import datos.UsuarioRepositorioTxt;
import entidades.Usuario;
import excepciones.AutenticacionFallidaException;
import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Pruebas del modulo de autenticacion.
 */
public class AutenticacionServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void debeAutenticarAdministradorPorDefecto() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "Admin123");
        assertNotNull(usuario);
    }

    @Test
    public void debeBloquearUsuarioTrasTresIntentosFallidos() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-bloqueo.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        for (int i = 0; i < 3; i++) {
            try {
                service.autenticar("admin", "MalaClave");
            } catch (AutenticacionFallidaException ignored) {
            }
        }

        Usuario usuario = repositorio.buscarPorId("admin").orElseThrow();
        assertTrue(usuario.isBloqueado());
    }
}
