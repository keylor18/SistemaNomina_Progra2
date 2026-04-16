package entidades;

import java.time.LocalDate;

/**
 * Representa a una persona colaboradora de la empresa.
 */
public class Empleado {

    private String id;
    private String cedula;
    private String nombreCompleto;
    private String puesto;
    private String departamento;
    private String correoElectronico;
    private double salarioBaseMensual;
    private int cantidadHijos;
    private boolean conyugeACargo;
    private LocalDate fechaIngreso;
    private boolean activo;

    public Empleado() {
    }

    public Empleado(String id, String cedula, String nombreCompleto, String puesto, String departamento,
            String correoElectronico, double salarioBaseMensual, int cantidadHijos, boolean conyugeACargo,
            LocalDate fechaIngreso, boolean activo) {
        this.id = id;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.puesto = puesto;
        this.departamento = departamento;
        this.correoElectronico = correoElectronico;
        this.salarioBaseMensual = salarioBaseMensual;
        this.cantidadHijos = cantidadHijos;
        this.conyugeACargo = conyugeACargo;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public double getSalarioBaseMensual() {
        return salarioBaseMensual;
    }

    public void setSalarioBaseMensual(double salarioBaseMensual) {
        this.salarioBaseMensual = salarioBaseMensual;
    }

    public int getCantidadHijos() {
        return cantidadHijos;
    }

    public void setCantidadHijos(int cantidadHijos) {
        this.cantidadHijos = cantidadHijos;
    }

    public boolean isConyugeACargo() {
        return conyugeACargo;
    }

    public void setConyugeACargo(boolean conyugeACargo) {
        this.conyugeACargo = conyugeACargo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombreCompleto + " (" + cedula + ")";
    }
}
