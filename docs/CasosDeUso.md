# Casos de Uso

## CU-01 Iniciar sesión

- Actor: Administrador o usuario.
- Precondición: existe un usuario en `usuarios.txt`.
- Flujo principal:
  El actor ingresa usuario y contraseña.
  El sistema valida el hash y el bloqueo.
  El sistema habilita el acceso.

## CU-02 Registrar empleado

- Actor: Administrador.
- Flujo principal:
  El actor completa el formulario.
  El sistema valida campos obligatorios y persistencia.
  El sistema guarda el empleado y actualiza la tabla.

## CU-03 Generar nómina

- Actor: Administrador.
- Flujo principal:
  El actor selecciona empleado y período.
  El sistema calcula deducciones y aportes.
  El sistema guarda la nómina y genera el PDF.

## CU-04 Enviar comprobante

- Actor: Administrador.
- Flujo principal:
  El actor selecciona una nómina.
  El sistema valida el correo del empleado.
  El sistema envía el PDF adjunto por SMTP.

## CU-05 Exportar reporte general

- Actor: Administrador.
- Flujo principal:
  El actor define el período.
  El sistema consolida todas las nóminas del mes.
  El sistema genera un PDF general.
