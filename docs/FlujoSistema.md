# Flujo Completo del Sistema

## 1. Inicio

- `presentacion.SistemaNomina_Progra2` prepara tema visual, carpetas y archivos.
- `presentacion.ContextoAplicacion` inicializa repositorios y servicios.
- `LoginController` presenta `LoginFrame`.

## 2. Autenticación

- Se leen usuarios desde `usuarios.txt`.
- Se compara la contraseña con SHA-256.
- Se controlan intentos fallidos y bloqueo.

## 3. Gestión de empleados

- `EmpleadoPanel` captura datos.
- `EmpleadoController` valida entradas.
- `EmpleadoService` aplica reglas de negocio.
- `EmpleadoRepositorioTxt` persiste el registro.

## 4. Nómina

- `NominaPanel` solicita empleado y período.
- `NominaService` calcula:
  salario bruto,
  deducciones del trabajador,
  renta,
  aportes patronales,
  salario neto.
- `NominaRepositorioTxt` guarda el historial.

## 5. Reportería y correo

- `ReporteNominaService` usa iText para PDF individual y general.
- `CorreoService` usa JavaMail para adjuntar y enviar comprobantes.
- `RegistroLogger` deja trazas en `logs.txt` si ocurre un error.
