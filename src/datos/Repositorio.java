package datos;

import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.util.List;
import java.util.Optional;

/**
 * Operaciones CRUD basicas para repositorios.
 *
 * @param <T> tipo de entidad
 * @param <ID> tipo de identificador
 */
public interface Repositorio<T, ID> {

    void guardar(T entidad) throws PersistenciaException, ValidacionException;

    List<T> obtenerTodos() throws PersistenciaException;

    Optional<T> buscarPorId(ID id) throws PersistenciaException;

    void actualizar(T entidad) throws PersistenciaException, ValidacionException, EntidadNoEncontradaException;

    void eliminar(ID id) throws PersistenciaException, EntidadNoEncontradaException;
}
