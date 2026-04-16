package datos;

import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementacion base de repositorios en archivos de texto.
 *
 * @param <T> tipo de entidad
 * @param <ID> tipo del identificador
 */
public abstract class RepositorioArchivo<T, ID> implements Repositorio<T, ID> {

    private final Object candado = new Object();
    private final Path rutaArchivo;

    protected RepositorioArchivo(Path rutaArchivo) throws PersistenciaException {
        this.rutaArchivo = rutaArchivo;
        try {
            if (Files.notExists(rutaArchivo)) {
                Files.createDirectories(rutaArchivo.getParent());
                Files.createFile(rutaArchivo);
            }
        } catch (IOException ex) {
            throw new PersistenciaException("No fue posible preparar el repositorio " + rutaArchivo.getFileName(), ex);
        }
    }

    @Override
    public void guardar(T entidad) throws PersistenciaException, ValidacionException {
        synchronized (candado) {
            List<T> entidades = leerEntidades();
            validarEntidad(entidad);
            if (buscarIndice(entidades, obtenerId(entidad)) >= 0) {
                throw new ValidacionException("Ya existe un registro con el identificador indicado.");
            }
            validarIntegridad(entidades, entidad, false);
            entidades.add(entidad);
            escribirEntidades(entidades);
        }
    }

    @Override
    public List<T> obtenerTodos() throws PersistenciaException {
        synchronized (candado) {
            return new ArrayList<>(leerEntidades());
        }
    }

    @Override
    public Optional<T> buscarPorId(ID id) throws PersistenciaException {
        synchronized (candado) {
            List<T> entidades = leerEntidades();
            int indice = buscarIndice(entidades, id);
            return indice >= 0 ? Optional.of(entidades.get(indice)) : Optional.empty();
        }
    }

    @Override
    public void actualizar(T entidad) throws PersistenciaException, ValidacionException, EntidadNoEncontradaException {
        synchronized (candado) {
            List<T> entidades = leerEntidades();
            validarEntidad(entidad);
            int indice = buscarIndice(entidades, obtenerId(entidad));
            if (indice < 0) {
                throw new EntidadNoEncontradaException("No existe un registro con el identificador indicado.");
            }
            validarIntegridad(entidades, entidad, true);
            entidades.set(indice, entidad);
            escribirEntidades(entidades);
        }
    }

    @Override
    public void eliminar(ID id) throws PersistenciaException, EntidadNoEncontradaException {
        synchronized (candado) {
            List<T> entidades = leerEntidades();
            int indice = buscarIndice(entidades, id);
            if (indice < 0) {
                throw new EntidadNoEncontradaException("No existe un registro con el identificador indicado.");
            }
            entidades.remove(indice);
            escribirEntidades(entidades);
        }
    }

    protected List<T> leerEntidades() throws PersistenciaException {
        try {
            List<String> lineas = Files.readAllLines(rutaArchivo, StandardCharsets.UTF_8);
            List<T> entidades = new ArrayList<>();
            int numeroLinea = 0;
            for (String linea : lineas) {
                numeroLinea++;
                if (linea == null || linea.isBlank()) {
                    continue;
                }
                entidades.add(deserializar(linea, numeroLinea));
            }
            return entidades;
        } catch (IOException ex) {
            throw new PersistenciaException("No fue posible leer el archivo " + rutaArchivo.getFileName(), ex);
        }
    }

    protected void escribirEntidades(List<T> entidades) throws PersistenciaException {
        Path temporal = rutaArchivo.resolveSibling(rutaArchivo.getFileName() + ".tmp");
        try {
            List<String> lineas = new ArrayList<>();
            for (T entidad : entidades) {
                lineas.add(serializar(entidad));
            }
            Files.write(temporal, lineas, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(temporal, rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new PersistenciaException("No fue posible escribir el archivo " + rutaArchivo.getFileName(), ex);
        }
    }

    private int buscarIndice(List<T> entidades, ID id) {
        for (int i = 0; i < entidades.size(); i++) {
            if (Objects.equals(obtenerId(entidades.get(i)), id)) {
                return i;
            }
        }
        return -1;
    }

    protected abstract ID obtenerId(T entidad);

    protected abstract void validarEntidad(T entidad) throws ValidacionException;

    protected abstract void validarIntegridad(List<T> entidades, T entidad, boolean esActualizacion)
            throws ValidacionException;

    protected abstract String serializar(T entidad);

    protected abstract T deserializar(String linea, int numeroLinea) throws PersistenciaException;
}
