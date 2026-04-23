package entidades;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Almacena el resultado de una liquidacion mensual de planilla.
 */
public class Nomina {

    private String id;
    private String empleadoId;
    private String nombreEmpleado;
    private YearMonth periodo;
    private LocalDate fechaGeneracion;
    private double salarioBaseOrdinario;
    private double horasExtra;
    private double montoHorasExtra;
    private double salarioBruto;
    private double deduccionSem;
    private double deduccionIvm;
    private double deduccionBancoPopular;
    private double deduccionRenta;
    private double totalDeducciones;
    private double aporteSemPatrono;
    private double aporteIvmPatrono;
    private double aporteAsignacionesFamiliares;
    private double aporteImas;
    private double aporteIna;
    private double aporteBancoPopularPatronal;
    private double aporteBancoPopularLpt;
    private double aporteFcl;
    private double aporteRop;
    private double aporteIns;
    private double totalAportesPatronales;
    private double salarioNeto;
    private String rutaPdf;

    public Nomina() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(String empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public YearMonth getPeriodo() {
        return periodo;
    }

    public void setPeriodo(YearMonth periodo) {
        this.periodo = periodo;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public double getSalarioBruto() {
        return salarioBruto;
    }

    public void setSalarioBruto(double salarioBruto) {
        this.salarioBruto = salarioBruto;
    }

    public double getSalarioBaseOrdinario() {
        return salarioBaseOrdinario;
    }

    public void setSalarioBaseOrdinario(double salarioBaseOrdinario) {
        this.salarioBaseOrdinario = salarioBaseOrdinario;
    }

    public double getHorasExtra() {
        return horasExtra;
    }

    public void setHorasExtra(double horasExtra) {
        this.horasExtra = horasExtra;
    }

    public double getMontoHorasExtra() {
        return montoHorasExtra;
    }

    public void setMontoHorasExtra(double montoHorasExtra) {
        this.montoHorasExtra = montoHorasExtra;
    }

    public double getDeduccionSem() {
        return deduccionSem;
    }

    public void setDeduccionSem(double deduccionSem) {
        this.deduccionSem = deduccionSem;
    }

    public double getDeduccionIvm() {
        return deduccionIvm;
    }

    public void setDeduccionIvm(double deduccionIvm) {
        this.deduccionIvm = deduccionIvm;
    }

    public double getDeduccionBancoPopular() {
        return deduccionBancoPopular;
    }

    public void setDeduccionBancoPopular(double deduccionBancoPopular) {
        this.deduccionBancoPopular = deduccionBancoPopular;
    }

    public double getDeduccionRenta() {
        return deduccionRenta;
    }

    public void setDeduccionRenta(double deduccionRenta) {
        this.deduccionRenta = deduccionRenta;
    }

    public double getTotalDeducciones() {
        return totalDeducciones;
    }

    public void setTotalDeducciones(double totalDeducciones) {
        this.totalDeducciones = totalDeducciones;
    }

    public double getAporteSemPatrono() {
        return aporteSemPatrono;
    }

    public void setAporteSemPatrono(double aporteSemPatrono) {
        this.aporteSemPatrono = aporteSemPatrono;
    }

    public double getAporteIvmPatrono() {
        return aporteIvmPatrono;
    }

    public void setAporteIvmPatrono(double aporteIvmPatrono) {
        this.aporteIvmPatrono = aporteIvmPatrono;
    }

    public double getAporteAsignacionesFamiliares() {
        return aporteAsignacionesFamiliares;
    }

    public void setAporteAsignacionesFamiliares(double aporteAsignacionesFamiliares) {
        this.aporteAsignacionesFamiliares = aporteAsignacionesFamiliares;
    }

    public double getAporteImas() {
        return aporteImas;
    }

    public void setAporteImas(double aporteImas) {
        this.aporteImas = aporteImas;
    }

    public double getAporteIna() {
        return aporteIna;
    }

    public void setAporteIna(double aporteIna) {
        this.aporteIna = aporteIna;
    }

    public double getAporteBancoPopularPatronal() {
        return aporteBancoPopularPatronal;
    }

    public void setAporteBancoPopularPatronal(double aporteBancoPopularPatronal) {
        this.aporteBancoPopularPatronal = aporteBancoPopularPatronal;
    }

    public double getAporteBancoPopularLpt() {
        return aporteBancoPopularLpt;
    }

    public void setAporteBancoPopularLpt(double aporteBancoPopularLpt) {
        this.aporteBancoPopularLpt = aporteBancoPopularLpt;
    }

    public double getAporteFcl() {
        return aporteFcl;
    }

    public void setAporteFcl(double aporteFcl) {
        this.aporteFcl = aporteFcl;
    }

    public double getAporteRop() {
        return aporteRop;
    }

    public void setAporteRop(double aporteRop) {
        this.aporteRop = aporteRop;
    }

    public double getAporteIns() {
        return aporteIns;
    }

    public void setAporteIns(double aporteIns) {
        this.aporteIns = aporteIns;
    }

    public double getTotalAportesPatronales() {
        return totalAportesPatronales;
    }

    public void setTotalAportesPatronales(double totalAportesPatronales) {
        this.totalAportesPatronales = totalAportesPatronales;
    }

    public double getSalarioNeto() {
        return salarioNeto;
    }

    public void setSalarioNeto(double salarioNeto) {
        this.salarioNeto = salarioNeto;
    }

    public String getRutaPdf() {
        return rutaPdf;
    }

    public void setRutaPdf(String rutaPdf) {
        this.rutaPdf = rutaPdf;
    }

    public double getCostoTotalEmpresa() {
        return salarioBruto + totalAportesPatronales;
    }
}
