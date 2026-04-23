# Manual de Usuario

## Ingreso al sistema

1. Abra la aplicacion desde NetBeans o con `ant run`.
2. Si es la primera ejecucion, ingrese con `admin / Admin123`.
3. El sistema puede mostrar una recomendacion para cambiar el usuario y la contrasena por defecto.
4. Si la autenticacion es correcta, se abre la ventana principal con los modulos `Colaboradores` y `Nomina y reportes`.

<img width="919" height="642" alt="Captura de pantalla 2026-04-22 a la(s) 7 22 55 p  m" src="https://github.com/user-attachments/assets/8651bae5-34b5-4557-bf98-5d1fe2079fc6" />


## Gestion de colaboradores

1. Complete cedula, nombre, puesto, departamento, correo, salario y fecha.
2. Presione `Guardar` para registrar al colaborador.
3. Seleccione una fila para cargarla nuevamente en el formulario.
4. Use `Actualizar` para modificar o `Eliminar` para depurar el registro.
5. Mantenga activos solo los colaboradores que participaran en el calculo de nomina.

## Generacion de nomina

1. Ingrese al modulo `Nomina y reportes`.
2. Seleccione un empleado activo.
3. Elija el periodo correspondiente.
4. Presione `Generar nomina`.
5. El sistema calcula salario bruto, deducciones, salario neto y costo patronal.
6. Se genera un PDF individual para el colaborador.

## Reporte patronal y correos

1. El comprobante del colaborador ya no mezcla los aportes patronales.
2. Si desea enviar informacion al patrono, escriba el correo en `Correo del patrono`.
3. Puede marcar `Enviar reporte patronal al correo indicado` para despacharlo al generar la nomina.
4. Tambien puede usar `Enviar correo` para reenviar el comprobante del colaborador.
5. Y puede usar `Enviar al patrono` para reenviar solo el reporte patronal separado.

## Reportes PDF

1. `PDF individual` regenera el comprobante del colaborador.
2. `PDF general` consolida todas las nominas del periodo seleccionado.
3. El detalle patronal queda separado del comprobante individual.

## Diagrama de flujo general

El diagrama completo del flujo del sistema se encuentra en PDF:

- [Diagrama_Flujo_SistemaNomina_Progra2.pdf](diagramas/Diagrama_Flujo_SistemaNomina_Progra2.pdf)
