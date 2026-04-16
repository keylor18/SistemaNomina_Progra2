# Guia Tecnica

## Estructura

- `app/`: arranque y contenedor de dependencias.
- `presentacion/`: `LoginFrame`, `MainFrame`, `EmpleadoPanel`, `NominaPanel`.
- `presentacion/controladores/`: controladores MVC para UI.
- `logica/`: servicios de autenticacion, empleados, nomina, reportes y correo.
- `datos/`: repositorios basados en archivos `.txt`.
- `utilidades/`: constantes, rutas, logger, seguridad, formato y serializacion.

## Persistencia

- `data/empleados.txt`
- `data/nominas.txt`
- `data/usuarios.txt`
- `data/logs.txt`

Los repositorios usan Base64 para serializar cadenas y separadores `|` para preservar integridad.

## Calculo de nomina

- Salario bruto: salario base mensual del empleado.
- Deducciones trabajador: SEM, IVM, Banco Popular e impuesto sobre la renta.
- Aportes patronales: SEM, IVM, Asignaciones Familiares, IMAS, INA, Banco Popular, FCL, ROP e INS.
- Creditos fiscales: hijos y conyuge a cargo.

## Librerias

- iText: generacion de PDF.
- JavaMail: envio de correos via SMTP SSL.
- JUnit: pruebas unitarias.

## Ejecucion

```powershell
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" clean test
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" run
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" javadoc
```
