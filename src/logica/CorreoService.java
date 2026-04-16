package logica;

import entidades.Empleado;
import entidades.Nomina;
import excepciones.CorreoException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import utilidades.ConstantesNomina;
import utilidades.FormatoUtil;
import utilidades.ValidacionesUtil;

/**
 * Envia comprobantes PDF por correo usando JavaMail.
 */
public class CorreoService extends LogicaBase implements NotificadorCorreo {

    private final DespachadorCorreo despachadorCorreo;

    public CorreoService() {
        this(Transport::send);
    }

    public CorreoService(DespachadorCorreo despachadorCorreo) {
        this.despachadorCorreo = despachadorCorreo;
    }

    @Override
    public void enviarNomina(Empleado empleado, Nomina nomina, Path rutaPdf) throws CorreoException {
        if (empleado == null || nomina == null) {
            throw new CorreoException("Debe indicar el empleado y la nomina a enviar.");
        }
        if (!ValidacionesUtil.esCorreoValido(empleado.getCorreoElectronico())) {
            throw new CorreoException("El empleado no tiene un correo valido.");
        }
        if (rutaPdf == null || Files.notExists(rutaPdf)) {
            throw new CorreoException("No se encontro el archivo PDF a adjuntar.");
        }

        try {
            Session session = Session.getInstance(crearPropiedades(), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(ConstantesNomina.REMITENTE_CORREO,
                            ConstantesNomina.CLAVE_CORREO);
                }
            });

            MimeMessage mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(ConstantesNomina.REMITENTE_CORREO));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empleado.getCorreoElectronico()));
            mensaje.setSubject("Comprobante de nomina - " + FormatoUtil.formatearPeriodo(nomina.getPeriodo()));

            MimeBodyPart cuerpo = new MimeBodyPart();
            cuerpo.setText("""
                    Estimado colaborador:

                    Se adjunta el comprobante de nomina correspondiente al periodo %s.

                    Salario neto: %s

                    Este mensaje fue generado automaticamente por el Sistema de Nomina Empresarial.
                    """
                    .formatted(FormatoUtil.formatearPeriodo(nomina.getPeriodo()),
                            FormatoUtil.formatearMoneda(nomina.getSalarioNeto())));

            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.attachFile(rutaPdf.toFile());

            MimeMultipart contenido = new MimeMultipart();
            contenido.addBodyPart(cuerpo);
            contenido.addBodyPart(adjunto);
            mensaje.setContent(contenido);

            despachadorCorreo.enviar(mensaje);
        } catch (Exception ex) {
            throw new CorreoException("No fue posible enviar el correo de la nomina.", ex);
        }
    }

    private Properties crearPropiedades() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "securemail.comredcr.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return props;
    }
}
