# Tarjeta de Fidelidad Gamificada

##  Descripción

Este proyecto corresponde a la Tarea 3 del curso INF331. Se trata de una aplicación de consola desarrollada en **Java 21** utilizando **Maven** y el enfoque de desarrollo **TDD (Test Driven Development)**. El sistema permite gestionar clientes y compras dentro de un programa de fidelización, asignando puntos y niveles según el historial de compras.

## Wiki

Puede acceder a la Wiki del proyecto mediante el siguiente [enlace](https://github.com/lmellan/Tarea_3-INF331/wiki).  


### Diseño General

Se implementó una arquitectura modular con separación de responsabilidades entre modelos, servicios, repositorios y la interfaz principal (`Main.java`). A continuación se presenta el diagrama de casos de uso que resume las principales funcionalidades del sistema:

![Diagrama de casos de uso](https://github.com/user-attachments/assets/55df4c5c-cf96-44ac-9b0f-d7ed1172a133)

##  Instalación del Proyecto

### 0. Clonar el repositorio

```bash
git clone https://github.com/lmellan/Tarea_3-INF331.git
cd Tarea_3-INF331/fidelidad-proyecto
```

### 1. Compilar e instalar dependencias

```bash
mvn clean install
```

### 2. Ejecutar el programa

```bash
mvn exec:java "-Dexec.mainClass=com.tiendafiel.fidelidad.Main"
```
>  Este comando ha sido probado exitosamente en PowerShell de Windows. Asegúrate de estar ubicado en la raíz del proyecto donde se encuentra el pom.xml.

### 3. Ejecutar pruebas

```bash
mvn test
```

Salida esperada:

```
[INFO] Tests run: 22, Failures: 0, Errors: 0, Skipped: 0
```


## Cómo usar

### 1. Menú principal de la aplicación (Consola)

Al ejecutar el programa verás lo siguiente:

```
======= Sistema de Fidelidad ========

1. Gestión de Clientes
2. Gestión de Compras
3. Mostrar Puntos / Nivel de un Cliente
4. Salir

Seleccione una opción:
```

### 2. Gestión de Clientes

Desde esta opción puedes:

* Agregar cliente
* Eliminar cliente
* Editar datos de cliente
* Ver detalles
* Listar todos los clientes registrados

### 3. Gestión de Compras

Puedes:

* Registrar compras para un cliente
* Ver detalles de una compra
* Listar todas las compras

### 4. Consultar Puntos y Nivel

Introduce el ID del cliente y el sistema mostrará:

* Total de puntos acumulados
* Nivel actual del cliente (Bronce, Plata, Oro, Platino)
* Multiplicador de puntos


## Análisis de Calidad

Este proyecto fue analizado con **SonarQube**, obteniendo el siguiente resultado:

- **Quality Gate:** Passed
- **Seguridad:** 0 issues
- **Mantenibilidad:** A (sin problemas abiertos)
- **Fiabilidad:** A (sin problemas abiertos)
- **Duplicaciones:** 0.0%
- **Cobertura de tests:** **100%**
 
## Estructura del Proyecto

La estructura de carpetas sigue una separación por capas:

```
FIDELIDAD-PROYECTO
|│
|├── src/
|   ├── main/java/com/tiendafiel/fidelidad
|   |   ├── models/               # Clases de dominio: Cliente y Compra
|   |   |   ├── Cliente.java
|   |   |   └── Compra.java
|   |   ├── repositories/         # Repositorios en memoria
|   |   |   ├── ClienteRepository.java
|   |   |   └── CompraRepository.java
|   |   ├── services/            # Lógica de negocio
|   |   |   └── CompraServices.java
|   |   └── Main.java             # Interfaz de consola
|   └── test/java/com/tiendafiel/fidelidad
|       └── MainTest.java         # Pruebas unitarias con JUnit 5
|└── pom.xml                      # Archivo de configuración Maven
```



## Licencia

Este proyecto está bajo la **MIT License** - ver el archivo [LICENSE](https://github.com/lmellan/Tarea_3-INF331/edit/main/LICENSE) para más detalles.

