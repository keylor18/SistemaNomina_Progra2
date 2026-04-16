# SistemaNomina_Progra2

Aplicacion de escritorio en Java Swing para gestionar empleados, calcular nominas con referencia normativa costarricense 2026, generar reportes PDF y enviar comprobantes por correo electronico.

## Tecnologias

- Java 17+ compatible con JDK 25
- Swing
- Apache Ant
- NetBeans (proyecto J2SE con `nbproject/`)
- iText 5.5.13.4
- JavaMail 1.6.2
- JUnit 4.13.2

## Arquitectura

- `presentacion/`: frames, paneles Swing y controladores
- `logica/`: reglas de negocio, autenticacion, nomina, PDF y correo
- `datos/`: repositorios `.txt`
- `entidades/`: modelo del dominio
- `excepciones/`: excepciones personalizadas
- `utilidades/`: rutas, hashing, formato, logging y constantes

## Funcionalidades

- Login con archivo de usuarios y bloqueo por intentos fallidos
- CRUD de empleados
- Calculo de nomina con salario bruto, deducciones, renta y aportes patronales
- Historial de nominas por empleado y periodo
- Generacion de PDF individual y general
- Envio de comprobantes por correo con adjunto PDF
- Logs de errores en `data/logs.txt`

## Credenciales iniciales

- Usuario: `admin`
- Contrasena: `Admin123`

## Compilacion y pruebas

Si `ant` no esta agregado al PATH, puede usar el que instala NetBeans:

```powershell
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" clean test
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" run
& "C:\Program Files\netbeans\extide\ant\bin\ant.bat" javadoc
```

## Referencias normativas usadas

- CCSS / SICERE / IVM vigente desde enero 2026: https://aissfa.ccss.sa.cr/noticias/noticia?v=822124515110
- Tramos de renta salarial 2026 del Ministerio de Hacienda: https://www.hacienda.go.cr/docs/TramosRenta2026.pdf

## Documentacion adicional

- [Manual de usuario](docs/ManualUsuario.md)
- [Guia tecnica](docs/GuiaTecnica.md)
- [Casos de uso](docs/CasosDeUso.md)
- [Flujo del sistema](docs/FlujoSistema.md)
