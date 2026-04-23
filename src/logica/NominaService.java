package logica;

import datos.EmpleadoRepositorioTxt;
import datos.NominaRepositorioTxt;
import entidades.Empleado;
import entidades.Nomina;
import excepciones.EntidadNoEncontradaException;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import utilidades.ConstantesNomina;
import utilidades.TramoRenta;

/**
 * Calcula y administra las nominas mensuales.
 */
public class NominaService extends LogicaBase implements CalculadoraNomina {

    private final NominaRepositorioTxt nominaRepositorio;
    private final EmpleadoRepositorioTxt empleadoRepositorio;

    public NominaService(NominaRepositorioTxt nominaRepositorio, EmpleadoRepositorioTxt empleadoRepositorio) {
        this.nominaRepositorio = nominaRepositorio;
        this.empleadoRepositorio = empleadoRepositorio;
    }

    @Override
    public Nomina calcularNomina(Empleado empleado, YearMonth periodo) throws ValidacionException {
        return calcularNomina(empleado, periodo, 0);
    }

    public Nomina calcularNomina(Empleado empleado, YearMonth periodo, double horasExtra)
            throws ValidacionException {
        validar(empleado != null, "Debe seleccionar un empleado.");
        validar(periodo != null, "Debe indicar el periodo de nomina.");
        validar(empleado.isActivo(), "Solo se puede generar nomina a empleados activos.");
        validar(horasExtra >= 0, "Las horas extra no pueden ser negativas.");

        double salarioBaseOrdinario = redondear(empleado.getSalarioBaseMensual());
        double horasExtraRedondeadas = redondear(horasExtra);
        double montoHorasExtra = redondear(horasExtraRedondeadas
                * (salarioBaseOrdinario / ConstantesNomina.HORAS_ORDINARIAS_MENSUALES)
                * ConstantesNomina.FACTOR_HORA_EXTRA);
        double salarioBruto = redondear(salarioBaseOrdinario + montoHorasExtra);
        double baseSem = Math.max(salarioBruto, ConstantesNomina.BASE_MINIMA_SEM_2026);
        double baseIvm = Math.max(salarioBruto, ConstantesNomina.BASE_MINIMA_IVM_2026);

        double deduccionSem = redondear(baseSem * ConstantesNomina.SEM_TRABAJADOR);
        double deduccionIvm = redondear(baseIvm * ConstantesNomina.IVM_TRABAJADOR);
        double deduccionBancoPopular = redondear(salarioBruto * ConstantesNomina.BANCO_POPULAR_TRABAJADOR);
        double deduccionRenta = redondear(calcularRentaSalarial(salarioBruto, empleado.getCantidadHijos(),
                empleado.isConyugeACargo()));
        double totalDeducciones = redondear(deduccionSem + deduccionIvm + deduccionBancoPopular + deduccionRenta);
        double salarioNeto = redondear(salarioBruto - totalDeducciones);

        double aporteSemPatrono = redondear(baseSem * ConstantesNomina.SEM_PATRONO);
        double aporteIvmPatrono = redondear(baseIvm * ConstantesNomina.IVM_PATRONO);
        double aporteAsignaciones = redondear(salarioBruto * ConstantesNomina.ASIGNACIONES_FAMILIARES);
        double aporteImas = redondear(salarioBruto * ConstantesNomina.IMAS);
        double aporteIna = redondear(salarioBruto * ConstantesNomina.INA);
        double aporteBancoPopularPatronal = redondear(salarioBruto * ConstantesNomina.BANCO_POPULAR_PATRONAL);
        double aporteBancoPopularLpt = redondear(salarioBruto * ConstantesNomina.BANCO_POPULAR_LPT_PATRONAL);
        double aporteFcl = redondear(salarioBruto * ConstantesNomina.FCL);
        double aporteRop = redondear(salarioBruto * ConstantesNomina.ROP);
        double aporteIns = redondear(salarioBruto * ConstantesNomina.INS);
        double totalAportes = redondear(aporteSemPatrono + aporteIvmPatrono + aporteAsignaciones + aporteImas
                + aporteIna + aporteBancoPopularPatronal + aporteBancoPopularLpt + aporteFcl + aporteRop + aporteIns);

        Nomina nomina = new Nomina();
        nomina.setId(generarId("NOM"));
        nomina.setEmpleadoId(empleado.getId());
        nomina.setNombreEmpleado(empleado.getNombreCompleto());
        nomina.setPeriodo(periodo);
        nomina.setFechaGeneracion(LocalDate.now());
        nomina.setSalarioBaseOrdinario(salarioBaseOrdinario);
        nomina.setHorasExtra(horasExtraRedondeadas);
        nomina.setMontoHorasExtra(montoHorasExtra);
        nomina.setSalarioBruto(salarioBruto);
        nomina.setDeduccionSem(deduccionSem);
        nomina.setDeduccionIvm(deduccionIvm);
        nomina.setDeduccionBancoPopular(deduccionBancoPopular);
        nomina.setDeduccionRenta(deduccionRenta);
        nomina.setTotalDeducciones(totalDeducciones);
        nomina.setAporteSemPatrono(aporteSemPatrono);
        nomina.setAporteIvmPatrono(aporteIvmPatrono);
        nomina.setAporteAsignacionesFamiliares(aporteAsignaciones);
        nomina.setAporteImas(aporteImas);
        nomina.setAporteIna(aporteIna);
        nomina.setAporteBancoPopularPatronal(aporteBancoPopularPatronal);
        nomina.setAporteBancoPopularLpt(aporteBancoPopularLpt);
        nomina.setAporteFcl(aporteFcl);
        nomina.setAporteRop(aporteRop);
        nomina.setAporteIns(aporteIns);
        nomina.setTotalAportesPatronales(totalAportes);
        nomina.setSalarioNeto(salarioNeto);
        nomina.setRutaPdf("");
        return nomina;
    }

    /**
     * Calcula, valida y persiste una nomina de empleado.
     *
     * @param empleadoId id del empleado
     * @param periodo periodo a liquidar
     * @return nomina persistida
     * @throws PersistenciaException si falla la persistencia
     * @throws ValidacionException si la nomina es invalida
     * @throws EntidadNoEncontradaException si el empleado no existe
     */
    public Nomina generarYGuardarNomina(String empleadoId, YearMonth periodo)
            throws PersistenciaException, ValidacionException, EntidadNoEncontradaException {
        return generarYGuardarNomina(empleadoId, periodo, 0);
    }

    public Nomina generarYGuardarNomina(String empleadoId, YearMonth periodo, double horasExtra)
            throws PersistenciaException, ValidacionException, EntidadNoEncontradaException {
        Empleado empleado = empleadoRepositorio.buscarPorId(empleadoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("El empleado seleccionado no existe."));
        List<Nomina> existentes = listarPorPeriodo(periodo);
        if (existentes.stream().anyMatch(n -> n.getEmpleadoId().equals(empleadoId))) {
            throw new ValidacionException("Ya existe una nomina para este empleado en el periodo seleccionado.");
        }
        Nomina nomina = calcularNomina(empleado, periodo, horasExtra);
        nominaRepositorio.guardar(nomina);
        return nomina;
    }

    /**
     * Actualiza la ruta del PDF generado para una nomina.
     *
     * @param nominaId nomina a actualizar
     * @param rutaPdf nueva ruta
     * @throws PersistenciaException si falla la persistencia
     * @throws EntidadNoEncontradaException si la nomina no existe
     * @throws ValidacionException si la actualizacion es invalida
     */
    public void actualizarRutaPdf(String nominaId, String rutaPdf)
            throws PersistenciaException, EntidadNoEncontradaException, ValidacionException {
        Nomina nomina = buscarPorId(nominaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("No se encontro la nomina solicitada."));
        nomina.setRutaPdf(rutaPdf);
        nominaRepositorio.actualizar(nomina);
    }

    /**
     * Lista todas las nominas generadas.
     *
     * @return historial ordenado
     * @throws PersistenciaException si falla la lectura
     */
    public List<Nomina> listarNominas() throws PersistenciaException {
        return nominaRepositorio.obtenerTodos().stream()
                .sorted(Comparator.comparing(Nomina::getPeriodo).reversed()
                        .thenComparing(Nomina::getNombreEmpleado, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    /**
     * Busca una nomina por su identificador.
     *
     * @param id identificador
     * @return nomina encontrada o vacia
     * @throws PersistenciaException si falla la lectura
     */
    public Optional<Nomina> buscarPorId(String id) throws PersistenciaException {
        return nominaRepositorio.buscarPorId(id);
    }

    /**
     * Lista las nominas de un empleado especifico.
     *
     * @param empleadoId empleado a consultar
     * @return historial del empleado
     * @throws PersistenciaException si falla la lectura
     */
    public List<Nomina> listarPorEmpleado(String empleadoId) throws PersistenciaException {
        return listarNominas().stream()
                .filter(nomina -> nomina.getEmpleadoId().equals(empleadoId))
                .collect(Collectors.toList());
    }

    /**
     * Elimina una nomina del historial por su identificador.
     *
     * @param nominaId id de la nomina a eliminar
     * @throws PersistenciaException si falla la persistencia
     * @throws EntidadNoEncontradaException si la nomina no existe
     */
    public void eliminarNomina(String nominaId) throws PersistenciaException, EntidadNoEncontradaException {
        nominaRepositorio.eliminar(nominaId);
    }

    /**
     * Lista nominas de un periodo mensual.
     *
     * @param periodo periodo a consultar
     * @return nominas del periodo
     * @throws PersistenciaException si falla la lectura
     */
    public List<Nomina> listarPorPeriodo(YearMonth periodo) throws PersistenciaException {
        return listarNominas().stream()
                .filter(nomina -> nomina.getPeriodo().equals(periodo))
                .collect(Collectors.toList());
    }

    private double calcularRentaSalarial(double salarioBruto, int hijos, boolean conyuge) {
        double impuesto = 0;
        double limiteAnterior = 0;
        for (TramoRenta tramo : ConstantesNomina.TRAMOS_RENTA_MENSUAL_2026) {
            if (salarioBruto <= limiteAnterior) {
                break;
            }
            double montoEnTramo = Math.min(salarioBruto, tramo.limiteSuperior()) - limiteAnterior;
            if (montoEnTramo > 0) {
                impuesto += montoEnTramo * tramo.tasa();
            }
            limiteAnterior = tramo.limiteSuperior();
        }
        double creditos = hijos * ConstantesNomina.CREDITO_HIJO_MENSUAL
                + (conyuge ? ConstantesNomina.CREDITO_CONYUGE_MENSUAL : 0);
        return Math.max(0, impuesto - creditos);
    }
}
