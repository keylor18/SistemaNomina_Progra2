package logica;

import entidades.Empleado;
import entidades.Nomina;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.concurrent.atomic.AtomicReference;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Pruebas del envio de correo.
 */
public class CorreoServiceTest {

    @Test
    public void debeConstruirCorreoConAdjunto() throws Exception {
        AtomicReference<MimeMessage> capturado = new AtomicReference<>();
        CorreoService service = new CorreoService(capturado::set);

        Path pdf = Files.createTempFile("nomina-prueba", ".pdf");
        Files.writeString(pdf, "contenido prueba");

        Empleado empleado = new Empleado("EMP-001", "101110111", "Ana Vargas", "Analista", "Finanzas",
                "ana@empresa.com", 1500000, 0, false, LocalDate.of(2024, 1, 10), true);
        Nomina nomina = new Nomina();
        nomina.setPeriodo(YearMonth.of(2026, 4));
        nomina.setSalarioNeto(1277710);

        service.enviarNomina(empleado, nomina, pdf);

        MimeMessage mensaje = capturado.get();
        assertNotNull(mensaje);
        assertEquals("ana@empresa.com",
                ((InternetAddress) mensaje.getRecipients(Message.RecipientType.TO)[0]).getAddress());
        assertTrue(mensaje.getSubject().contains("04/2026"));
        assertTrue(mensaje.getContent() instanceof Multipart);
        assertEquals(2, ((Multipart) mensaje.getContent()).getCount());
    }

    @Test
    public void debeConstruirCorreoPatronalConAdjuntoSeparado() throws Exception {
        AtomicReference<MimeMessage> capturado = new AtomicReference<>();
        CorreoService service = new CorreoService(capturado::set);

        Path pdf = Files.createTempFile("patronal-prueba", ".pdf");
        Files.writeString(pdf, "contenido patronal");

        Nomina nomina = new Nomina();
        nomina.setNombreEmpleado("Ana Vargas");
        nomina.setPeriodo(YearMonth.of(2026, 4));
        nomina.setTotalAportesPatronales(402950);
        nomina.setSalarioBruto(1500000);

        service.enviarReportePatronal("patrono@empresa.com", nomina, pdf);

        MimeMessage mensaje = capturado.get();
        assertNotNull(mensaje);
        assertEquals("patrono@empresa.com",
                ((InternetAddress) mensaje.getRecipients(Message.RecipientType.TO)[0]).getAddress());
        assertTrue(mensaje.getSubject().contains("Reporte patronal"));
        assertTrue(mensaje.getContent() instanceof Multipart);
        assertEquals(2, ((Multipart) mensaje.getContent()).getCount());
    }
}
