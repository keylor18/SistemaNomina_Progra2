package logica;

import entidades.Empleado;
import entidades.Nomina;
import excepciones.CorreoException;
import java.nio.file.Path;

/**
 * Contrato para envio de notificaciones por correo.
 */
public interface NotificadorCorreo {

    void enviarNomina(Empleado empleado, Nomina nomina, Path rutaPdf) throws CorreoException;

    void enviarReportePatronal(String correoPatrono, Nomina nomina, Path rutaPdf) throws CorreoException;
}
