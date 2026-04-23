package datos;

import entidades.RolUsuario;
import entidades.Usuario;
import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import utilidades.TextoPlanoUtil;
import utilidades.ValidacionesUtil;

/**
 * Persistencia de usuarios del sistema en texto plano estructurado.
 */
public class UsuarioRepositorioTxt extends RepositorioArchivo<Usuario, String> {

    public UsuarioRepositorioTxt(Path rutaArchivo) throws PersistenciaException {
        super(rutaArchivo);
    }

    @Override
    protected String obtenerId(Usuario entidad) {
        return entidad.getUsername();
    }

    @Override
    protected void validarEntidad(Usuario entidad) throws ValidacionException {
        if (entidad == null) {
            throw new ValidacionException("El usuario no puede ser nulo.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getUsername())) {
            throw new ValidacionException("El nombre de usuario es obligatorio.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getNombreCompleto())) {
            throw new ValidacionException("El nombre completo del usuario es obligatorio.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getPasswordHash())) {
            throw new ValidacionException("La contrasena cifrada es obligatoria.");
        }
        if (entidad.getRol() == null) {
            throw new ValidacionException("El rol del usuario es obligatorio.");
        }
        if (entidad.getIntentosFallidos() < 0) {
            throw new ValidacionException("Los intentos fallidos no pueden ser negativos.");
        }
    }

    @Override
    protected void validarIntegridad(List<Usuario> entidades, Usuario entidad, boolean esActualizacion)
            throws ValidacionException {
        for (Usuario actual : entidades) {
            boolean esMismo = Objects.equals(actual.getUsername(), entidad.getUsername());
            if (!esActualizacion || !esMismo) {
                if (actual.getUsername().equalsIgnoreCase(entidad.getUsername())) {
                    throw new ValidacionException("Ya existe un usuario con el mismo nombre de acceso.");
                }
            }
        }
    }

    public void actualizar(String usernameOriginal, Usuario entidad)
            throws PersistenciaException, ValidacionException, EntidadNoEncontradaException {
        synchronized (candado) {
            List<Usuario> entidades = leerEntidades();
            validarEntidad(entidad);
            int indiceOriginal = buscarIndicePorUsername(entidades, usernameOriginal);
            if (indiceOriginal < 0) {
                throw new EntidadNoEncontradaException("No existe un usuario con el identificador indicado.");
            }
            validarIntegridadActualizacion(entidades, entidad, usernameOriginal);
            entidades.set(indiceOriginal, entidad);
            escribirEntidades(entidades);
        }
    }

    @Override
    protected String serializar(Usuario entidad) {
        return String.join("|",
                TextoPlanoUtil.codificarCampo(entidad.getUsername()),
                TextoPlanoUtil.codificarCampo(entidad.getNombreCompleto()),
                TextoPlanoUtil.codificarCampo(entidad.getPasswordHash()),
                entidad.getRol().name(),
                Integer.toString(entidad.getIntentosFallidos()),
                Boolean.toString(entidad.isBloqueado()));
    }

    @Override
    protected Usuario deserializar(String linea, int numeroLinea) throws PersistenciaException {
        String[] partes = linea.split("\\|", -1);
        if (partes.length != 6) {
            throw new PersistenciaException("Registro de usuario invalido en la linea " + numeroLinea + ".");
        }
        try {
            return new Usuario(
                    TextoPlanoUtil.decodificarCampo(partes[0]),
                    TextoPlanoUtil.decodificarCampo(partes[1]),
                    TextoPlanoUtil.decodificarCampo(partes[2]),
                    RolUsuario.valueOf(partes[3]),
                    Integer.parseInt(partes[4]),
                    Boolean.parseBoolean(partes[5]));
        } catch (Exception ex) {
            throw new PersistenciaException("No fue posible interpretar el usuario de la linea " + numeroLinea + ".", ex);
        }
    }

    private int buscarIndicePorUsername(List<Usuario> entidades, String username) {
        for (int i = 0; i < entidades.size(); i++) {
            if (Objects.equals(entidades.get(i).getUsername(), username)) {
                return i;
            }
        }
        return -1;
    }

    private void validarIntegridadActualizacion(List<Usuario> entidades, Usuario entidad, String usernameOriginal)
            throws ValidacionException {
        for (Usuario actual : entidades) {
            boolean esMismoRegistro = actual.getUsername().equalsIgnoreCase(usernameOriginal);
            if (!esMismoRegistro && actual.getUsername().equalsIgnoreCase(entidad.getUsername())) {
                throw new ValidacionException("Ya existe un usuario con el mismo nombre de acceso.");
            }
        }
    }
}
