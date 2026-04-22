package utilidades;

/**
 * Representa un tramo progresivo del impuesto sobre la renta salarial.
 *
 * @param limiteSuperior limite superior del tramo. Use
 * {@link Double#POSITIVE_INFINITY} para el ultimo tramo.
 * @param tasa porcentaje aplicable al exceso dentro del tramo
 */
public record TramoRenta(double limiteSuperior, double tasa) {
}
