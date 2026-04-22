# Manual de Usuario

## Ingreso al sistema

1. Abra la aplicación desde NetBeans o con `ant run`.
2. Ingrese con `admin / Admin123`.
3. El sistema mostrará las pestañas de empleados y nómina.

## Gestión de empleados

1. Complete cédula, nombre, puesto, departamento, correo, salario y fecha.
2. Presione `Guardar` para registrar.
3. Seleccione una fila para cargarla en el formulario.
4. Use `Actualizar` o `Eliminar` según corresponda.

## Generación de nómina

1. Seleccione un empleado activo.
2. Elija el período.
3. Presione `Generar nómina`.
4. El sistema calcula la nómina, crea el PDF y actualiza el historial.
5. Si marca `Enviar PDF automáticamente al generar`, también lo enviará al correo del empleado.

## Reportes y correo

1. Seleccione una nómina del historial para ver su detalle.
2. Use `Exportar PDF individual` para regenerar el comprobante.
3. Use `Exportar PDF general` para consolidar el período seleccionado.
4. Use `Enviar correo` para reenviar un comprobante individual.
