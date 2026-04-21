# Sistema Nomina Progra2

> Aplicacion de escritorio en Java Swing para administrar colaboradores, calcular nomina, generar comprobantes PDF y enviar resultados por correo electronico desde una sola interfaz.

---

## Explicación General

SistemaNomina_Progra2 es un proyecto orientado a la gestion administrativa de personal y planilla. El sistema trabaja con persistencia local en archivos `.txt`, aplica reglas de negocio para empleados y nomina, y presenta todo el flujo en una interfaz de escritorio construida con Swing.

### Lo que resuelve

- Registro, consulta, actualizacion y eliminacion de empleados.
- Generacion de nomina por empleado y periodo.
- Calculo de deducciones, renta y aportes patronales.
- Emision de comprobantes PDF individuales y reportes generales.
- Envio de comprobantes por correo electronico.
- Registro de errores operativos en archivos locales.

### Tecnologias principales

| Tecnologia | Uso dentro del proyecto |
| --- | --- |
| Java 17+ | Base del sistema y logica de negocio |
| Swing | Interfaz grafica de escritorio |
| Apache Ant | Compilacion, pruebas y ejecucion |
| NetBeans | Estructura J2SE del proyecto |
| iText 5.5.13.4 | Generacion de reportes PDF |
| JavaMail 1.6.2 | Envio de comprobantes por correo |
| JUnit 4.13.2 | Pruebas automatizadas |

---

## Como Funciona El Sistema

El sistema sigue un flujo claro desde el arranque hasta la generacion de reportes:

### 1. Inicio de la aplicacion

- `presentacion.SistemaNomina_Progra2` es el punto de entrada.
- Se instala el tema visual.
- Se crea automaticamente la estructura de carpetas y archivos necesarios.
- Se inicializa `ContextoAplicacion`, que conecta repositorios y servicios.
- Se muestra la ventana de acceso `LoginFrame`.

### 2. Autenticacion

- `LoginController` recibe el usuario y la contrasena ingresados.
- `AutenticacionService` consulta `data/usuarios.txt`.
- La contrasena se compara con su hash SHA-256.
- Se controlan intentos fallidos y bloqueo del usuario.
- Si no existe ningun usuario, el sistema crea automaticamente uno administrador inicial.

### 3. Modulo de colaboradores

Desde la pestana `Colaboradores`, el usuario puede:

- Registrar empleados nuevos.
- Editar informacion existente.
- Eliminar registros.
- Marcar empleados como activos o inactivos.
- Consultar una tabla con resumen y metricas de planilla.

Este flujo pasa por:

- `EmpleadoPanel` para captura y visualizacion.
- `EmpleadoController` para coordinar eventos.
- `EmpleadoService` para validaciones y reglas de negocio.
- `EmpleadoRepositorioTxt` para guardar en `data/empleados.txt`.

### 4. Modulo de nomina y reportes

Desde la pestana `Nomina y reportes`, el usuario puede:

- Seleccionar un empleado activo.
- Elegir un periodo.
- Generar una nomina.
- Consultar el historial almacenado.
- Regenerar PDF individuales.
- Crear un PDF general por periodo.
- Reenviar comprobantes por correo.

Este flujo pasa por:

- `NominaPanel` para la interfaz de operacion.
- `NominaController` para coordinar acciones.
- `NominaService` para calcular salario bruto, deducciones, renta, aportes patronales y salario neto.
- `NominaRepositorioTxt` para guardar resultados en `data/nominas.txt`.
- `ReporteNominaService` para crear los PDFs en `reportes/`.
- `CorreoService` para enviar el comprobante al correo del empleado.

### 5. Persistencia y trazabilidad

El proyecto no depende de una base de datos externa. Toda la informacion se almacena en archivos locales:

| Ruta | Contenido |
| --- | --- |
| `data/usuarios.txt` | Usuarios del sistema |
| `data/empleados.txt` | Catalogo de empleados |
| `data/nominas.txt` | Historial de nominas generadas |
| `data/logs.txt` | Registro de errores |
| `reportes/` | PDFs individuales y generales |

---

## Arquitectura Del Proyecto

La aplicacion esta organizada por capas para separar interfaz, logica y persistencia:

| Carpeta | Responsabilidad |
| --- | --- |
| `src/presentacion/` | Ventanas, paneles y experiencia visual |
| `src/presentacion/controladores/` | Coordinacion entre UI y servicios |
| `src/logica/` | Reglas de negocio, autenticacion, nomina, PDF y correo |
| `src/datos/` | Repositorios basados en archivos `.txt` |
| `src/entidades/` | Modelos del dominio |
| `src/excepciones/` | Excepciones personalizadas |
| `src/utilidades/` | Hashing, rutas, formato, constantes y logging |

---

## Flujo De Uso

### Ingreso al sistema

1. Ejecute la aplicacion.
2. Ingrese con las credenciales iniciales.
3. Si la autenticacion es correcta, se abre el panel principal.

### Gestion de empleados

1. Complete los datos del colaborador.
2. Guarde el registro.
3. Seleccione un empleado de la tabla para editar o eliminar.
4. Mantenga activos solo los colaboradores que deban participar en nomina.

### Generacion de nomina

1. Vaya a la pestana `Nomina y reportes`.
2. Seleccione un empleado activo.
3. Elija el periodo correspondiente.
4. Presione `Generar nomina`.
5. El sistema calcula, guarda, genera el PDF y actualiza el historial.
6. Si activa el envio automatico, tambien despacha el comprobante por correo.

---

## Credenciales Iniciales

Si `data/usuarios.txt` esta vacio, el sistema crea automaticamente este usuario:

- Usuario: `admin`
- Contrasena: `Admin123`
- Rol: `ADMIN`

---

## Referencias Normativas Utilizadas

- CCSS / SICERE / IVM vigente desde enero 2026: https://aissfa.ccss.sa.cr/noticias/noticia?v=822124515110
- Tramos de renta salarial 2026 del Ministerio de Hacienda: https://www.hacienda.go.cr/docs/TramosRenta2026.pdf

---

## Documentacion Adicional

- [Manual de usuario](docs/ManualUsuario.md)
- [Guia tecnica](docs/GuiaTecnica.md)
- [Casos de uso](docs/CasosDeUso.md)
- [Flujo del sistema](docs/FlujoSistema.md)

---

## Resumen Final

SistemaNomina_Progra2 combina interfaz de escritorio, persistencia local y servicios de negocio para cubrir el ciclo basico de una planilla:

- autenticar usuarios,
- administrar empleados,
- calcular nomina,
- generar reportes,
- y compartir comprobantes por correo.

Es un proyecto util para estudiar arquitectura por capas, Swing, persistencia en archivos y automatizacion de procesos administrativos en Java.
