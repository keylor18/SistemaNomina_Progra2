package logica;

import datos.UsuarioRepositorioTxt;
import entidades.Usuario;
import excepciones.AutenticacionFallidaException;
import excepciones.ValidacionException;
import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Pruebas del módulo de autenticación.
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

    @Test
    public void debeDetectarCredencialesPorDefectoDelAdministrador() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-default.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "Admin123");

        assertTrue(service.usaCredencialesPorDefecto(usuario));
    }

    @Test
    public void debeCambiarContrasenaYDesactivarCredencialesPorDefecto() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-cambio.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "Admin123");
        service.cambiarContrasena(usuario, "V4l!dA2026#");

        Usuario autenticado = service.autenticar("admin", "V4l!dA2026#");
        assertNotNull(autenticado);
        assertFalse(service.usaCredencialesPorDefecto(autenticado));
    }

    @Test(expected = ValidacionException.class)
    public void debeRechazarCambioConPalabraComunDeDiccionario() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-palabra.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "Admin123");
        service.cambiarContrasena(usuario, "Admin!2026AZ");
    }
}
