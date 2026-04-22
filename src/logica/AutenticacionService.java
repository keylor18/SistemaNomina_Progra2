package logica;

import datos.UsuarioRepositorioTxt;
import entidades.RolUsuario;
import entidades.Usuario;
import excepciones.AutenticacionFallidaException;
import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.util.Optional;
import utilidades.ConstantesNomina;
import utilidades.RegistroLogger;
import utilidades.SeguridadUtil;
import utilidades.ValidacionesUtil;

/**
 * Gestiona el inicio de sesion basado en archivo de usuarios.
 */
public class AutenticacionService extends LogicaBase {

    private final UsuarioRepositorioTxt usuarioRepositorio;

    public AutenticacionService(UsuarioRepositorioTxt usuarioRepositorio) throws PersistenciaException, ValidacionException {
        this.usuarioRepositorio = usuarioRepositorio;
        asegurarUsuarioAdministrador();
    }

    /**
     * Garantiza que el sistema cuente con un usuario administrador inicial.
     *
     * @throws PersistenciaException si falla la persistencia
     * @throws ValidacionException si el usuario por defecto es invalido
     */
    public final void asegurarUsuarioAdministrador() throws PersistenciaException, ValidacionException {
        if (usuarioRepositorio.obtenerTodos().isEmpty()) {
            Usuario admin = new Usuario("admin", "Administrador General",
                    SeguridadUtil.hashSha256("Admin123"), RolUsuario.ADMIN, 0, false);
            usuarioRepositorio.guardar(admin);
        }
    }

    /**
     * Autentica un usuario y actualiza sus intentos fallidos.
     *
     * @param username usuario ingresado
     * @param password contrasena ingresada
     * @return usuario autenticado
     * @throws AutenticacionFallidaException cuando no se puede autenticar
     * @throws PersistenciaException si falla la persistencia
     */
    public Usuario autenticar(String username, String password)
            throws AutenticacionFallidaException, PersistenciaException {
        if (!ValidacionesUtil.tieneTexto(username) || !ValidacionesUtil.tieneTexto(password)) {
            throw new AutenticacionFallidaException("Debe ingresar usuario y contrasena.");
        }
        String usuarioNormalizado = username.trim().toLowerCase();
        Optional<Usuario> encontrado = usuarioRepositorio.buscarPorId(usuarioNormalizado);
        if (encontrado.isEmpty()) {
            throw new AutenticacionFallidaException("Usuario o contrasena incorrectos.");
        }

        Usuario usuario = encontrado.get();
        if (usuario.isBloqueado()) {
            throw new AutenticacionFallidaException("El usuario esta bloqueado por intentos fallidos.");
        }

        String hash = SeguridadUtil.hashSha256(password);
        if (!usuario.getPasswordHash().equals(hash)) {
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            if (usuario.getIntentosFallidos() >= ConstantesNomina.MAX_INTENTOS_LOGIN) {
                usuario.setBloqueado(true);
            }
            persistirUsuarioSilenciosamente(usuario);
            if (usuario.isBloqueado()) {
                throw new AutenticacionFallidaException("Usuario bloqueado por exceder el maximo de intentos.");
            }
            throw new AutenticacionFallidaException("Usuario o contrasena incorrectos.");
        }

        if (usuario.getIntentosFallidos() > 0 || usuario.isBloqueado()) {
            usuario.setIntentosFallidos(0);
            usuario.setBloqueado(false);
            persistirUsuarioSilenciosamente(usuario);
        }
        return usuario;
    }

    /**
     * Reinicia el contador de intentos del usuario indicado.
     *
     * @param username nombre de usuario
     * @throws PersistenciaException si falla la persistencia
     * @throws EntidadNoEncontradaException si el usuario no existe
     * @throws ValidacionException si la actualizacion es invalida
     */
    public void desbloquearUsuario(String username)
            throws PersistenciaException, EntidadNoEncontradaException, ValidacionException {
        Usuario usuario = usuarioRepositorio.buscarPorId(username.toLowerCase())
                .orElseThrow(() -> new EntidadNoEncontradaException("Usuario no encontrado."));
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
        usuarioRepositorio.actualizar(usuario);
    }

    private void persistirUsuarioSilenciosamente(Usuario usuario) throws PersistenciaException {
        try {
            usuarioRepositorio.actualizar(usuario);
        } catch (EntidadNoEncontradaException | ValidacionException ex) {
            RegistroLogger.registrarError("Actualizacion de usuario en autenticacion", ex);
            throw new PersistenciaException("No fue posible actualizar el estado del usuario.", ex);
        }
    }
}
