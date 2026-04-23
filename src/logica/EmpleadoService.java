package logica;

import datos.EmpleadoRepositorioTxt;
import entidades.Empleado;
import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import utilidades.ValidacionesUtil;

/**
 * Gestiona las operaciones de negocio relacionadas con empleados.
 */
public class EmpleadoService extends LogicaBase {

    private final EmpleadoRepositorioTxt empleadoRepositorio;

    public EmpleadoService(EmpleadoRepositorioTxt empleadoRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
    }

    /**
     * Registra un nuevo empleado.
     *
     * @param empleado entidad a registrar
     * @return empleado persistido con id garantizado
     * @throws PersistenciaException si falla el almacenamiento
     * @throws ValidacionException si la informacion no cumple las reglas
     */
    public Empleado crearEmpleado(Empleado empleado) throws PersistenciaException, ValidacionException {
        validarEmpleado(empleado);
        if (!ValidacionesUtil.tieneTexto(empleado.getId())) {
            empleado.setId(generarId("EMP"));
        }
        empleadoRepositorio.guardar(empleado);
        return empleado;
    }

    /**
     * Actualiza un empleado existente.
     *
     * @param empleado empleado actualizado
     * @throws PersistenciaException si falla la persistencia
     * @throws ValidacionException si la entidad es invalida
     * @throws EntidadNoEncontradaException si el empleado no existe
     */
    public void actualizarEmpleado(Empleado empleado)
            throws PersistenciaException, ValidacionException, EntidadNoEncontradaException {
        validarEmpleado(empleado);
        empleadoRepositorio.actualizar(empleado);
    }

    /**
     * Elimina un empleado por su identificador.
     *
     * @param id identificador del empleado
     * @throws PersistenciaException si falla la persistencia
     * @throws EntidadNoEncontradaException si el empleado no existe
     */
    public void eliminarEmpleado(String id) throws PersistenciaException, EntidadNoEncontradaException {
        empleadoRepositorio.eliminar(id);
    }

    /**
     * Obtiene todos los empleados ordenados por nombre.
     *
     * @return lista ordenada
     * @throws PersistenciaException si falla la lectura
     */
    public List<Empleado> listarEmpleados() throws PersistenciaException {
        return empleadoRepositorio.obtenerTodos().stream()
                .sorted(Comparator.comparing(Empleado::getNombreCompleto, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los empleados activos.
     *
     * @return lista de empleados activos
     * @throws PersistenciaException si falla la lectura
     */
    public List<Empleado> listarActivos() throws PersistenciaException {
        return listarEmpleados().stream()
                .filter(Empleado::isActivo)
                .collect(Collectors.toList());
    }

    /**
     * Busca un empleado por su identificador.
     *
     * @param id identificador a consultar
     * @return empleado encontrado o vacio
     * @throws PersistenciaException si falla la lectura
     */
    public Optional<Empleado> buscarPorId(String id) throws PersistenciaException {
        return empleadoRepositorio.buscarPorId(id);
    }

    private void validarEmpleado(Empleado empleado) throws ValidacionException {
        validar(empleado != null, "Debe proporcionar la informacion del empleado.");
        validar(ValidacionesUtil.tieneTexto(empleado.getCedula()), "La cedula es obligatoria.");
        validar(ValidacionesUtil.tieneTexto(empleado.getNombreCompleto()), "El nombre es obligatorio.");
        validar(ValidacionesUtil.tieneTexto(empleado.getPuesto()), "El puesto es obligatorio.");
        validar(ValidacionesUtil.tieneTexto(empleado.getDepartamento()), "El departamento es obligatorio.");
        validar(ValidacionesUtil.esCorreoValido(empleado.getCorreoElectronico()), "El correo electronico no es valido.");
        validar(empleado.getSalarioBaseMensual() > 0, "El salario base debe ser mayor a cero.");
        validar(empleado.getCantidadHijos() >= 0, "La cantidad de hijos no puede ser negativa.");
        validar(empleado.getDiasVacacionesAsignados() >= 0,
                "Los dias de vacaciones asignados no pueden ser negativos.");
        validar(empleado.getDiasVacacionesTomados() >= 0,
                "Los dias de vacaciones solicitados no pueden ser negativos.");
        validar(empleado.getDiasVacacionesTomados() <= empleado.getDiasVacacionesAsignados(),
                "Los dias de vacaciones solicitados no pueden superar los dias disponibles.");
        if (empleado.getFechaIngreso() == null) {
            empleado.setFechaIngreso(LocalDate.now());
        }
    }
}
