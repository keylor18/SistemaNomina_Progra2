package datos;

import entidades.Empleado;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import utilidades.TextoPlanoUtil;
import utilidades.ValidacionesUtil;

/**
 * Persistencia de empleados en archivo de texto.
 */
public class EmpleadoRepositorioTxt extends RepositorioArchivo<Empleado, String> {

    public EmpleadoRepositorioTxt(Path rutaArchivo) throws PersistenciaException {
        super(rutaArchivo);
    }

    @Override
    protected String obtenerId(Empleado entidad) {
        return entidad.getId();
    }

    @Override
    protected void validarEntidad(Empleado entidad) throws ValidacionException {
        if (entidad == null) {
            throw new ValidacionException("El empleado no puede ser nulo.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getId())) {
            throw new ValidacionException("El identificador del empleado es obligatorio.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getCedula())) {
            throw new ValidacionException("La cedula del empleado es obligatoria.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getNombreCompleto())) {
            throw new ValidacionException("El nombre del empleado es obligatorio.");
        }
        if (!ValidacionesUtil.esCorreoValido(entidad.getCorreoElectronico())) {
            throw new ValidacionException("El correo electronico del empleado no es valido.");
        }
        if (entidad.getSalarioBaseMensual() <= 0) {
            throw new ValidacionException("El salario base debe ser mayor que cero.");
        }
        if (entidad.getCantidadHijos() < 0) {
            throw new ValidacionException("La cantidad de hijos no puede ser negativa.");
        }
        if (entidad.getFechaIngreso() == null) {
            throw new ValidacionException("La fecha de ingreso es obligatoria.");
        }
    }

    @Override
    protected void validarIntegridad(List<Empleado> entidades, Empleado entidad, boolean esActualizacion)
            throws ValidacionException {
        for (Empleado actual : entidades) {
            boolean esMismo = Objects.equals(actual.getId(), entidad.getId());
            if (!esActualizacion || !esMismo) {
                if (actual.getCedula().equalsIgnoreCase(entidad.getCedula())) {
                    throw new ValidacionException("Ya existe otro empleado con la misma cedula.");
                }
                if (actual.getCorreoElectronico().equalsIgnoreCase(entidad.getCorreoElectronico())) {
                    throw new ValidacionException("Ya existe otro empleado con el mismo correo.");
                }
            }
        }
    }

    @Override
    protected String serializar(Empleado entidad) {
        return String.join("|",
                TextoPlanoUtil.codificarCampo(entidad.getId()),
                TextoPlanoUtil.codificarCampo(entidad.getCedula()),
                TextoPlanoUtil.codificarCampo(entidad.getNombreCompleto()),
                TextoPlanoUtil.codificarCampo(entidad.getPuesto()),
                TextoPlanoUtil.codificarCampo(entidad.getDepartamento()),
                TextoPlanoUtil.codificarCampo(entidad.getCorreoElectronico()),
                String.format(Locale.US, "%.2f", entidad.getSalarioBaseMensual()),
                Integer.toString(entidad.getCantidadHijos()),
                Boolean.toString(entidad.isConyugeACargo()),
                entidad.getFechaIngreso().toString(),
                Boolean.toString(entidad.isActivo()));
    }

    @Override
    protected Empleado deserializar(String linea, int numeroLinea) throws PersistenciaException {
        String[] partes = linea.split("\\|", -1);
        if (partes.length != 11) {
            throw new PersistenciaException("Registro de empleado invalido en la linea " + numeroLinea + ".");
        }
        try {
            return new Empleado(
                    TextoPlanoUtil.decodificarCampo(partes[0]),
                    TextoPlanoUtil.decodificarCampo(partes[1]),
                    TextoPlanoUtil.decodificarCampo(partes[2]),
                    TextoPlanoUtil.decodificarCampo(partes[3]),
                    TextoPlanoUtil.decodificarCampo(partes[4]),
                    TextoPlanoUtil.decodificarCampo(partes[5]),
                    Double.parseDouble(partes[6]),
                    Integer.parseInt(partes[7]),
                    Boolean.parseBoolean(partes[8]),
                    LocalDate.parse(partes[9]),
                    Boolean.parseBoolean(partes[10]));
        } catch (Exception ex) {
            throw new PersistenciaException("No fue posible interpretar el empleado de la linea " + numeroLinea + ".", ex);
        }
    }
}
