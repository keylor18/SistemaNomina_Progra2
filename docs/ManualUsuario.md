# Manual de Usuario

## Ingreso al sistema

1. Abra la aplicacion desde NetBeans o con `ant run`.
2. Ingrese con `admin / Admin123`.
3. El sistema mostrara las pestanas de empleados y nomina.

## Gestion de empleados

1. Complete cedula, nombre, puesto, departamento, correo, salario y fecha.
2. Presione `Guardar` para registrar.
3. Seleccione una fila para cargarla en el formulario.
4. Use `Actualizar` o `Eliminar` segun corresponda.

## Generacion de nomina

1. Seleccione un empleado activo.
2. Elija el periodo.
3. Presione `Generar nomina`.
4. El sistema calcula la nomina, crea el PDF y actualiza el historial.
5. Si marca `Enviar PDF automaticamente al generar`, tambien lo enviara al correo del empleado.

## Reportes y correo

1. Seleccione una nomina del historial para ver su detalle.
2. Use `Exportar PDF individual` para regenerar el comprobante.
3. Use `Exportar PDF general` para consolidar el periodo seleccionado.
4. Use `Enviar correo` para reenviar un comprobante individual.
