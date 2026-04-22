package presentacion;

import datos.EmpleadoRepositorioTxt;
import datos.NominaRepositorioTxt;
import datos.UsuarioRepositorioTxt;
import excepciones.PersistenciaException;
import excepciones.ValidacionException;
import logica.AutenticacionService;
import logica.CorreoService;
import logica.EmpleadoService;
import logica.NominaService;
import logica.ReporteNominaService;
import utilidades.RutasSistema;

/**
 * Contenedor de dependencias inicializado desde la capa de presentacion.
 */
public class ContextoAplicacion {

    private final EmpleadoRepositorioTxt empleadoRepositorio;
    private final NominaRepositorioTxt nominaRepositorio;
    private final UsuarioRepositorioTxt usuarioRepositorio;
    private final AutenticacionService autenticacionService;
    private final EmpleadoService empleadoService;
    private final NominaService nominaService;
    private final ReporteNominaService reporteNominaService;
    private final CorreoService correoService;

    public ContextoAplicacion() throws PersistenciaException, ValidacionException {
        empleadoRepositorio = new EmpleadoRepositorioTxt(RutasSistema.EMPLEADOS);
        nominaRepositorio = new NominaRepositorioTxt(RutasSistema.NOMINAS);
        usuarioRepositorio = new UsuarioRepositorioTxt(RutasSistema.USUARIOS);
        autenticacionService = new AutenticacionService(usuarioRepositorio);
        empleadoService = new EmpleadoService(empleadoRepositorio);
        nominaService = new NominaService(nominaRepositorio, empleadoRepositorio);
        reporteNominaService = new ReporteNominaService();
        correoService = new CorreoService();
    }

    public AutenticacionService getAutenticacionService() {
        return autenticacionService;
    }

    public EmpleadoService getEmpleadoService() {
        return empleadoService;
    }

    public NominaService getNominaService() {
        return nominaService;
    }

    public ReporteNominaService getReporteNominaService() {
        return reporteNominaService;
    }

    public CorreoService getCorreoService() {
        return correoService;
    }
}
