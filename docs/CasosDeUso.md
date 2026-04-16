# Casos de Uso

## CU-01 Iniciar sesion

- Actor: Administrador o usuario.
- Precondicion: existe un usuario en `usuarios.txt`.
- Flujo principal:
  El actor ingresa usuario y contrasena.
  El sistema valida el hash y el bloqueo.
  El sistema habilita el acceso.

## CU-02 Registrar empleado

- Actor: Administrador.
- Flujo principal:
  El actor completa el formulario.
  El sistema valida campos obligatorios y persistencia.
  El sistema guarda el empleado y actualiza la tabla.

## CU-03 Generar nomina

- Actor: Administrador.
- Flujo principal:
  El actor selecciona empleado y periodo.
  El sistema calcula deducciones y aportes.
  El sistema guarda la nomina y genera el PDF.

## CU-04 Enviar comprobante

- Actor: Administrador.
- Flujo principal:
  El actor selecciona una nomina.
  El sistema valida el correo del empleado.
  El sistema envia el PDF adjunto por SMTP.

## CU-05 Exportar reporte general

- Actor: Administrador.
- Flujo principal:
  El actor define el periodo.
  El sistema consolida todas las nominas del mes.
  El sistema genera un PDF general.
