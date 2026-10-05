# HelpDesk – Gestión de incidencias

Aplicación de consola desarrollada en **Java** para la gestión de incidencias de un centro educativo.

El programa permite crear, consultar, buscar y cerrar incidencias, además de mostrar estadísticas y guardar los datos para conservarlos entre diferentes ejecuciones.

## 1. Requisitos

- Java **17** (ajusta si usas otra versión; debe coincidir con `pom.xml`)
- Maven **3.x** (comprobar con `mvn -v`)
- JUnit **5** (se descarga automáticamente con Maven)
## 2. Compilación, pruebas y ejecución

```bash
# Compilar
mvn compile
 
# Ejecutar todas las pruebas
mvn test
 
# Ejecutar la aplicación (desde IntelliJ: botón ▶ en AplicacionHelpDesk)
mvn compile exec:java -Dexec.mainClass="HelpDesk.AplicacionHelpDesk"
```
> `exec:java` requiere el plugin `exec-maven-plugin`. Si no está en el `pom.xml`, ejecuta la clase `AplicacionHelpDesk` directamente desde el IDE.

Las carpetas `target/` e `.idea/` no se entregan: están excluidas mediante `.gitignore`.