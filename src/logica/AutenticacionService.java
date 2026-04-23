package logica;

import datos.UsuarioRepositorioTxt;
import entidades.RolUsuario;
import entidades.Usuario;
import excepciones.AutenticacionFallidaException;
import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.util.List;
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
     * Indica si ya debe mostrarse la opción de recuperación de contraseña.
     *
     * @return true cuando ya no existen credenciales iniciales activas
     * @throws PersistenciaException si falla la lectura de usuarios
     */
    public boolean debeMostrarRecuperacionContrasena() throws PersistenciaException {
        List<Usuario> usuarios = usuarioRepositorio.obtenerTodos();
        return !usuarios.isEmpty() && usuarios.stream().noneMatch(this::usaCredencialesPorDefecto);
    }

    /**
     * Fuerza el reemplazo de las credenciales iniciales del sistema.
     *
     * @param usuario usuario autenticado con la clave inicial
     * @param nuevoUsername nuevo nombre de acceso
     * @param nuevaContrasena nueva contraseña segura
     * @throws PersistenciaException si falla la persistencia
     * @throws ValidacionException si no se reemplaza la contraseña inicial
     */
    public void actualizarCredencialesIniciales(Usuario usuario, String nuevoUsername, String nuevaContrasena)
            throws PersistenciaException, ValidacionException {
        if (usuario == null) {
            throw new ValidacionException("El usuario autenticado es obligatorio.");
        }
        if (usaCredencialesPorDefecto(usuario)
                && SeguridadUtil.hashSha256(nuevaContrasena).equals(usuario.getPasswordHash())) {
            throw new ValidacionException("Debe definir una contraseña diferente a la credencial inicial.");
        }
        actualizarCredencialesAcceso(usuario, nuevoUsername, nuevaContrasena);
    }

    /**
     * Actualiza el usuario de acceso y la contraseña del usuario autenticado.
     *
     * @param usuario usuario autenticado
     * @param nuevoUsername nuevo nombre de acceso
     * @param nuevaContrasena nueva contraseña segura
     * @throws PersistenciaException si falla la persistencia
     * @throws ValidacionException si el nuevo acceso es inválido
     */
    public void actualizarCredencialesAcceso(Usuario usuario, String nuevoUsername, String nuevaContrasena)
            throws PersistenciaException, ValidacionException {
        if (usuario == null) {
            throw new ValidacionException("El usuario autenticado es obligatorio.");
        }

        String usernameActual = usuario.getUsername();
        String usernameNormalizado = ValidacionesUtil.normalizarUsername(nuevoUsername);
        ValidacionesUtil.validarContrasenaSegura(nuevaContrasena);

        String nuevoHash = SeguridadUtil.hashSha256(nuevaContrasena);
        boolean mismoUsername = usernameNormalizado.equalsIgnoreCase(usernameActual);
        boolean mismaContrasena = nuevoHash.equals(usuario.getPasswordHash());
        if (mismoUsername && mismaContrasena) {
            throw new ValidacionException("Debe actualizar al menos el nombre de usuario o la contraseña.");
        }

        Usuario actualizado = new Usuario(usernameNormalizado, usuario.getNombreCompleto(), nuevoHash,
                usuario.getRol(), 0, false);

        try {
            usuarioRepositorio.actualizar(usernameActual, actualizado);
        } catch (EntidadNoEncontradaException ex) {
            RegistroLogger.registrarError("Actualización de credenciales de acceso", ex);
            throw new PersistenciaException("No fue posible actualizar las credenciales del usuario.", ex);
        }

        usuario.setUsername(usernameNormalizado);
        usuario.setPasswordHash(nuevoHash);
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
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
        actualizarCredencialesAcceso(usuario, usuario.getUsername(), nuevaContrasena);
    }

    /**
     * Permite recuperar la contraseña validando el usuario y el nombre completo.
     *
     * @param username nombre de usuario actual
     * @param nombreCompleto nombre completo registrado en la cuenta
     * @param nuevaContrasena nueva contraseña segura
     * @throws PersistenciaException si falla la persistencia
     * @throws ValidacionException si los datos no son válidos
     */
    public void restablecerContrasenaOlvidada(String username, String nombreCompleto, String nuevaContrasena)
            throws PersistenciaException, ValidacionException {
        String usernameNormalizado = ValidacionesUtil.normalizarUsername(username);
        if (!ValidacionesUtil.tieneTexto(nombreCompleto)) {
            throw new ValidacionException("El nombre completo es obligatorio para recuperar la contraseña.");
        }

        Usuario usuario;
        try {
            usuario = usuarioRepositorio.buscarPorId(usernameNormalizado)
                    .orElseThrow(() -> new EntidadNoEncontradaException("No se encontró un usuario con ese nombre de acceso."));
        } catch (EntidadNoEncontradaException ex) {
            throw new ValidacionException(ex.getMessage());
        }

        if (!ValidacionesUtil.sonTextosEquivalentes(usuario.getNombreCompleto(), nombreCompleto)) {
            throw new ValidacionException("El nombre completo no coincide con la cuenta indicada.");
        }
        if (usaCredencialesPorDefecto(usuario)) {
            throw new ValidacionException("Primero debe cambiar las credenciales iniciales desde el ingreso principal.");
        }

        cambiarContrasena(usuario, nuevaContrasena);
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
        usuarioRepositorio.actualizar(usuario.getUsername(), usuario);
    }

    private void persistirUsuarioSilenciosamente(Usuario usuario) throws PersistenciaException {
        try {
            usuarioRepositorio.actualizar(usuario.getUsername(), usuario);
        } catch (EntidadNoEncontradaException | ValidacionException ex) {
            RegistroLogger.registrarError("Actualización de usuario en autenticación", ex);
            throw new PersistenciaException("No fue posible actualizar el estado del usuario.", ex);
        }
    }
}
