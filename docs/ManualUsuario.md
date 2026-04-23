# Manual de Usuario

## Ingreso al sistema

1. Abra la aplicacion desde NetBeans o con `ant run`.
2. Si es la primera ejecucion, ingrese con `admin / Admin123`.
3. El sistema puede mostrar una recomendacion para cambiar el usuario y la contrasena por defecto.
4. Si la autenticacion es correcta, se abre la ventana principal con los modulos `Colaboradores` y `Nomina y reportes`.

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

```mermaid
flowchart TD
    A([Inicio<br/>presentacion.SistemaNomina_Progra2.main()])
    B[Instalar tema visual<br/>Inicializar carpetas y archivos]
    C[Crear ContextoAplicacion<br/>Repositorios + servicios]
    D[Mostrar LoginFrame]
    E{Usuario y contrasena validos?}
    F[Mostrar error<br/>Aumentar intentos fallidos<br/>Bloquear si aplica]
    G[UsuarioRepositorioTxt<br/>data/usuarios.txt]
    H{Credenciales por defecto?}
    I[Recomendar cambio de usuario<br/>y contrasena segura]
    J[Mostrar MainFrame]
    K{Modulo seleccionado}

    subgraph EMP[Modulo de Colaboradores]
        L1[EmpleadoPanel + EmpleadoController]
        L2[EmpleadoService<br/>Valida datos y reglas]
        L3[EmpleadoRepositorioTxt<br/>data/empleados.txt]
        L4[Recargar tablas y lista<br/>de empleados activos]
        L1 --> L2 --> L3 --> L4
    end

    subgraph NOM[Modulo de Nomina y Reportes]
        N1[NominaPanel<br/>Seleccionar empleado y periodo]
        N2[NominaService<br/>Calcular bruto, deducciones,<br/>aportes patronales y neto]
        N3[NominaRepositorioTxt<br/>data/nominas.txt]
        N4[ReporteNominaService<br/>PDF individual del colaborador]
        N5[ReporteNominaService<br/>PDF patronal separado]
        N6{Enviar automaticamente al colaborador?}
        N7[CorreoService<br/>Enviar comprobante al colaborador]
        N8{Enviar al patrono?}
        N9[CorreoService<br/>Enviar reporte patronal separado]
        N10[Reporte general PDF por periodo]

        N1 --> N2 --> N3 --> N4 --> N6
        N6 -- Si --> N7
        N6 -- No --> N8
        N7 --> N8
        N8 -- Si --> N5 --> N9
        N8 -- No --> N10
        N9 --> N10
    end

    Z[RegistroLogger<br/>data/logs.txt]

    A --> B --> C --> D --> E
    E -- No --> F --> D
    E -- Si --> G --> H
    H -- Si --> I --> J
    H -- No --> J
    J --> K
    K -->|Colaboradores| L1
    K -->|Nomina y reportes| N1
    L4 -. Sincroniza empleados activos .-> N1
    L2 -. Errores .-> Z
    N2 -. Errores .-> Z
    N4 -. Errores .-> Z
    N5 -. Errores .-> Z
    N7 -. Errores .-> Z
    N9 -. Errores .-> Z
```
