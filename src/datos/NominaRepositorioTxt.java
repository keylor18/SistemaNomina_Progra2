package datos;

import entidades.Nomina;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import utilidades.TextoPlanoUtil;
import utilidades.ValidacionesUtil;

/**
 * Persistencia de nominas generadas.
 */
public class NominaRepositorioTxt extends RepositorioArchivo<Nomina, String> {

    public NominaRepositorioTxt(Path rutaArchivo) throws PersistenciaException {
        super(rutaArchivo);
    }

    @Override
    protected String obtenerId(Nomina entidad) {
        return entidad.getId();
    }

    @Override
    protected void validarEntidad(Nomina entidad) throws ValidacionException {
        if (entidad == null) {
            throw new ValidacionException("La nomina no puede ser nula.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getId())) {
            throw new ValidacionException("El identificador de la nomina es obligatorio.");
        }
        if (!ValidacionesUtil.tieneTexto(entidad.getEmpleadoId())) {
            throw new ValidacionException("La nomina debe estar asociada a un empleado.");
        }
        if (entidad.getPeriodo() == null) {
            throw new ValidacionException("El periodo de la nomina es obligatorio.");
        }
        if (entidad.getFechaGeneracion() == null) {
            throw new ValidacionException("La fecha de generacion es obligatoria.");
        }
        if (entidad.getSalarioBruto() <= 0) {
            throw new ValidacionException("El salario bruto debe ser mayor a cero.");
        }
        if (entidad.getHorasExtra() < 0) {
            throw new ValidacionException("Las horas extra no pueden ser negativas.");
        }
        if (entidad.getMontoHorasExtra() < 0) {
            throw new ValidacionException("El monto por horas extra no puede ser negativo.");
        }
    }

    @Override
    protected void validarIntegridad(List<Nomina> entidades, Nomina entidad, boolean esActualizacion)
            throws ValidacionException {
        for (Nomina actual : entidades) {
            boolean mismoPeriodo = actual.getEmpleadoId().equals(entidad.getEmpleadoId())
                    && actual.getPeriodo().equals(entidad.getPeriodo());
            boolean mismaNomina = actual.getId().equals(entidad.getId());
            if ((!esActualizacion || !mismaNomina) && mismoPeriodo) {
                throw new ValidacionException("Ya existe una nomina para ese empleado en el periodo indicado.");
            }
        }
    }

    @Override
    protected String serializar(Nomina entidad) {
        return String.join("|",
                TextoPlanoUtil.codificarCampo(entidad.getId()),
                TextoPlanoUtil.codificarCampo(entidad.getEmpleadoId()),
                TextoPlanoUtil.codificarCampo(entidad.getNombreEmpleado()),
                entidad.getPeriodo().toString(),
                entidad.getFechaGeneracion().toString(),
                formatear(entidad.getSalarioBruto()),
                formatear(entidad.getDeduccionSem()),
                formatear(entidad.getDeduccionIvm()),
                formatear(entidad.getDeduccionBancoPopular()),
                formatear(entidad.getDeduccionRenta()),
                formatear(entidad.getTotalDeducciones()),
                formatear(entidad.getAporteSemPatrono()),
                formatear(entidad.getAporteIvmPatrono()),
                formatear(entidad.getAporteAsignacionesFamiliares()),
                formatear(entidad.getAporteImas()),
                formatear(entidad.getAporteIna()),
                formatear(entidad.getAporteBancoPopularPatronal()),
                formatear(entidad.getAporteBancoPopularLpt()),
                formatear(entidad.getAporteFcl()),
                formatear(entidad.getAporteRop()),
                formatear(entidad.getAporteIns()),
                formatear(entidad.getTotalAportesPatronales()),
                formatear(entidad.getSalarioNeto()),
                TextoPlanoUtil.codificarCampo(entidad.getRutaPdf()),
                formatear(entidad.getSalarioBaseOrdinario()),
                formatear(entidad.getHorasExtra()),
                formatear(entidad.getMontoHorasExtra()));
    }

    @Override
    protected Nomina deserializar(String linea, int numeroLinea) throws PersistenciaException {
        String[] partes = linea.split("\\|", -1);
        if (partes.length != 24 && partes.length != 27) {
            throw new PersistenciaException("Registro de nomina invalido en la linea " + numeroLinea + ".");
        }
        try {
            Nomina nomina = new Nomina();
            nomina.setId(TextoPlanoUtil.decodificarCampo(partes[0]));
            nomina.setEmpleadoId(TextoPlanoUtil.decodificarCampo(partes[1]));
            nomina.setNombreEmpleado(TextoPlanoUtil.decodificarCampo(partes[2]));
            nomina.setPeriodo(YearMonth.parse(partes[3]));
            nomina.setFechaGeneracion(LocalDate.parse(partes[4]));
            nomina.setSalarioBruto(Double.parseDouble(partes[5]));
            nomina.setDeduccionSem(Double.parseDouble(partes[6]));
            nomina.setDeduccionIvm(Double.parseDouble(partes[7]));
            nomina.setDeduccionBancoPopular(Double.parseDouble(partes[8]));
            nomina.setDeduccionRenta(Double.parseDouble(partes[9]));
            nomina.setTotalDeducciones(Double.parseDouble(partes[10]));
            nomina.setAporteSemPatrono(Double.parseDouble(partes[11]));
            nomina.setAporteIvmPatrono(Double.parseDouble(partes[12]));
            nomina.setAporteAsignacionesFamiliares(Double.parseDouble(partes[13]));
            nomina.setAporteImas(Double.parseDouble(partes[14]));
            nomina.setAporteIna(Double.parseDouble(partes[15]));
            nomina.setAporteBancoPopularPatronal(Double.parseDouble(partes[16]));
            nomina.setAporteBancoPopularLpt(Double.parseDouble(partes[17]));
            nomina.setAporteFcl(Double.parseDouble(partes[18]));
            nomina.setAporteRop(Double.parseDouble(partes[19]));
            nomina.setAporteIns(Double.parseDouble(partes[20]));
            nomina.setTotalAportesPatronales(Double.parseDouble(partes[21]));
            nomina.setSalarioNeto(Double.parseDouble(partes[22]));
            nomina.setRutaPdf(TextoPlanoUtil.decodificarCampo(partes[23]));
            if (partes.length == 27) {
                nomina.setSalarioBaseOrdinario(Double.parseDouble(partes[24]));
                nomina.setHorasExtra(Double.parseDouble(partes[25]));
                nomina.setMontoHorasExtra(Double.parseDouble(partes[26]));
            } else {
                nomina.setSalarioBaseOrdinario(nomina.getSalarioBruto());
                nomina.setHorasExtra(0);
                nomina.setMontoHorasExtra(0);
            }
            return nomina;
        } catch (Exception ex) {
            throw new PersistenciaException("No fue posible interpretar la nomina de la linea " + numeroLinea + ".", ex);
        }
    }

    private String formatear(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
}
