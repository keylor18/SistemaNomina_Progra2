# Guía Técnica

## Estructura

- `presentacion/`: arranque, composición, frames y paneles principales.
- `presentacion/controladores/`: controladores MVC para UI.
- `presentacion/componentes/`: tarjetas, gradientes y renderizadores reutilizables.
- `presentacion/estilo/`: paleta y reglas visuales compartidas.
- `logica/`: servicios de autenticación, empleados, nómina, reportes y correo.
- `datos/`: repositorios basados en archivos `.txt`.
- `utilidades/`: constantes, rutas, logger, seguridad, formato y serialización.

## Persistencia

- `data/empleados.txt`
- `data/nominas.txt`
- `data/usuarios.txt`
- `data/logs.txt`

Los repositorios usan Base64 para serializar cadenas y separadores `|` para preservar integridad.

## Cálculo de nómina

- Salario bruto: salario base mensual del empleado.
- Deducciones trabajador: SEM, IVM, Banco Popular e impuesto sobre la renta.
- Aportes patronales: SEM, IVM, Asignaciones Familiares, IMAS, INA, Banco Popular, FCL, ROP e INS.
- Créditos fiscales: hijos y cónyuge a cargo.

## Librerías

- iText: generación de PDF.
- JavaMail: envío de correos vía SMTP SSL.
- JUnit: pruebas unitarias.

## Ejecución

```powershell
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" clean test
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" run
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" javadoc
```
