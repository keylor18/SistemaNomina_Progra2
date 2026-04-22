package logica;

import excepciones.PdfException;
import java.nio.file.Path;

/**
 * Contrato comun para exportacion de reportes PDF.
 *
 * @param <T> entidad de negocio exportable
 */
public interface ExportadorPdf<T> {

    Path generarReporte(T entidad) throws PdfException;
}
