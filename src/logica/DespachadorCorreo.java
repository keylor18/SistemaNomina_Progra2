package logica;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Punto de extension para desacoplar el envio real de JavaMail.
 */
@FunctionalInterface
public interface DespachadorCorreo {

    void enviar(MimeMessage mensaje) throws MessagingException;
}
