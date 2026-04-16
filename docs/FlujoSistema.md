# Flujo Completo del Sistema

## 1. Inicio

- `presentacion.Examen2_Programa2` prepara tema visual, carpetas y archivos.
- `presentacion.ContextoAplicacion` inicializa repositorios y servicios.
- `LoginController` presenta `LoginFrame`.

## 2. Autenticacion

- Se leen usuarios desde `usuarios.txt`.
- Se compara la contrasena con SHA-256.
- Se controlan intentos fallidos y bloqueo.

## 3. Gestion de empleados

- `EmpleadoPanel` captura datos.
- `EmpleadoController` valida entradas.
- `EmpleadoService` aplica reglas de negocio.
- `EmpleadoRepositorioTxt` persiste el registro.

## 4. Nomina

- `NominaPanel` solicita empleado y periodo.
- `NominaService` calcula:
  salario bruto,
  deducciones del trabajador,
  renta,
  aportes patronales,
  salario neto.
- `NominaRepositorioTxt` guarda el historial.

## 5. Reporteria y correo

- `ReporteNominaService` usa iText para PDF individual y general.
- `CorreoService` usa JavaMail para adjuntar y enviar comprobantes.
- `RegistroLogger` deja trazas en `logs.txt` si ocurre un error.
