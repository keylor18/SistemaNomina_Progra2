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

        Usuario usuario = service.autenticar("admin", "admin");
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

        Usuario usuario = service.autenticar("admin", "admin");

        assertTrue(service.usaCredencialesPorDefecto(usuario));
    }

    @Test
    public void debeCambiarUsuarioYContrasenaIniciales() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-cambio-inicial.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "admin");
        service.actualizarCredencialesIniciales(usuario, "admin.seguro", "V4l!dA2026#");

        Usuario autenticado = service.autenticar("admin.seguro", "V4l!dA2026#");
        assertNotNull(autenticado);
        assertFalse(service.usaCredencialesPorDefecto(autenticado));
    }

    @Test(expected = ValidacionException.class)
    public void debeRechazarReutilizarLaContrasenaInicialAunqueCambieElUsuario() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-reutiliza-inicial.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "admin");
        service.actualizarCredencialesIniciales(usuario, "admin.seguro", "admin");
    }

    @Test
    public void debeRecuperarContrasenaDespuesDeCambiarCredencialesIniciales() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-recuperacion.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "admin");
        service.actualizarCredencialesIniciales(usuario, "admin.seguro", "V4l!dA2026#");

        service.restablecerContrasenaOlvidada("admin.seguro", "Administrador General", "Risc0!Delta27");

        Usuario autenticado = service.autenticar("admin.seguro", "Risc0!Delta27");
        assertNotNull(autenticado);
    }

    @Test(expected = ValidacionException.class)
    public void debeRechazarRecuperacionConNombreCompletoIncorrecto() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-recuperacion-invalida.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "admin");
        service.actualizarCredencialesIniciales(usuario, "admin.seguro", "V4l!dA2026#");

        service.restablecerContrasenaOlvidada("admin.seguro", "Nombre Incorrecto", "Risc0!Delta27");
    }

    @Test(expected = ValidacionException.class)
    public void debeRechazarCambioConPalabraComunDeDiccionario() throws Exception {
        File archivo = temporaryFolder.newFile("usuarios-palabra.txt");
        UsuarioRepositorioTxt repositorio = new UsuarioRepositorioTxt(archivo.toPath());
        AutenticacionService service = new AutenticacionService(repositorio);

        Usuario usuario = service.autenticar("admin", "admin");
        service.cambiarContrasena(usuario, "Admin!2026AZ");
    }
}
