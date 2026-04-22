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
import utilidades.ConstantesSeguridad;
import utilidades.RegistroLogger;
import utilidades.SeguridadUtil;
import utilidades.ValidacionesUtil;

/**
 * Gestiona el inicio de sesión basado en archivo de usuarios.
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
     * @throws ValidacionException si el usuario por defecto es inválido
     */
    public final void asegurarUsuarioAdministrador() throws PersistenciaException, ValidacionException {
        if (usuarioRepositorio.obtenerTodos().isEmpty()) {
            Usuario admin = new Usuario(ConstantesSeguridad.USUARIO_ADMIN_POR_DEFECTO,
                    ConstantesSeguridad.NOMBRE_ADMIN_POR_DEFECTO,
                    SeguridadUtil.hashSha256(ConstantesSeguridad.CONTRASENA_ADMIN_POR_DEFECTO),
                    RolUsuario.ADMIN, 0, false);
            usuarioRepositorio.guardar(admin);
        }
    }

    /**
     * Autentica un usuario y actualiza sus intentos fallidos.
     *
     * @param username usuario ingresado
     * @param password contraseña ingresada
     * @return usuario autenticado
     * @throws AutenticacionFallidaException cuando no se puede autenticar
     * @throws PersistenciaException si falla la persistencia
     */
    public Usuario autenticar(String username, String password)
            throws AutenticacionFallidaException, PersistenciaException {
        if (!ValidacionesUtil.tieneTexto(username) || !ValidacionesUtil.tieneTexto(password)) {
            throw new AutenticacionFallidaException("Debe ingresar usuario y contraseña.");
        }
        String usuarioNormalizado = username.trim().toLowerCase();
        Optional<Usuario> encontrado = usuarioRepositorio.buscarPorId(usuarioNormalizado);
        if (encontrado.isEmpty()) {
            throw new AutenticacionFallidaException("Usuario o contraseña incorrectos.");
        }

        Usuario usuario = encontrado.get();
        if (usuario.isBloqueado()) {
            throw new AutenticacionFallidaException("El usuario está bloqueado por intentos fallidos.");
        }

        String hash = SeguridadUtil.hashSha256(password);
        if (!usuario.getPasswordHash().equals(hash)) {
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            if (usuario.getIntentosFallidos() >= ConstantesNomina.MAX_INTENTOS_LOGIN) {
                usuario.setBloqueado(true);
            }
            persistirUsuarioSilenciosamente(usuario);
            if (usuario.isBloqueado()) {
                throw new AutenticacionFallidaException("Usuario bloqueado por exceder el máximo de intentos.");
            }
            throw new AutenticacionFallidaException("Usuario o contraseña incorrectos.");
        }

        if (usuario.getIntentosFallidos() > 0 || usuario.isBloqueado()) {
            usuario.setIntentosFallidos(0);
            usuario.setBloqueado(false);
            persistirUsuarioSilenciosamente(usuario);
        }
        return usuario;
    }

    /**
     * Indica si el usuario sigue operando con la credencial inicial del sistema.
     *
     * @param usuario usuario autenticado
     * @return true si aún conserva la clave predeterminada
     */
    public boolean usaCredencialesPorDefecto(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        return ConstantesSeguridad.USUARIO_ADMIN_POR_DEFECTO.equalsIgnoreCase(usuario.getUsername())
                && SeguridadUtil.hashSha256(ConstantesSeguridad.CONTRASENA_ADMIN_POR_DEFECTO)
                        .equals(usuario.getPasswordHash());
    }

    /**
     * Actualiza la contraseña del usuario autenticado.
     *
     * @param usuario usuario que cambiará su clave
     * @param nuevaContrasena nueva contraseña en texto plano
     * @throws PersistenciaException si falla la persistencia
     * @throws ValidacionException si la nueva contraseña es inválida
     */
    public void cambiarContrasena(Usuario usuario, String nuevaContrasena)
            throws PersistenciaException, ValidacionException {
        if (usuario == null) {
            throw new ValidacionException("El usuario autenticado es obligatorio.");
        }

        ValidacionesUtil.validarContrasenaSegura(nuevaContrasena);
        String nuevoHash = SeguridadUtil.hashSha256(nuevaContrasena);
        if (nuevoHash.equals(usuario.getPasswordHash())) {
            throw new ValidacionException("La nueva contraseña debe ser diferente a la actual.");
        }

        usuario.setPasswordHash(nuevoHash);
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
        try {
            usuarioRepositorio.actualizar(usuario);
        } catch (EntidadNoEncontradaException ex) {
            RegistroLogger.registrarError("Cambio de contraseña de usuario", ex);
            throw new PersistenciaException("No fue posible actualizar la contraseña del usuario.", ex);
        }
    }

    /**
     * Reinicia el contador de intentos del usuario indicado.
     *
     * @param username nombre de usuario
     * @throws PersistenciaException si falla la persistencia
     * @throws EntidadNoEncontradaException si el usuario no existe
     * @throws ValidacionException si la actualización es inválida
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
            RegistroLogger.registrarError("Actualización de usuario en autenticación", ex);
            throw new PersistenciaException("No fue posible actualizar el estado del usuario.", ex);
        }
    }
}
