# GestorProcesos

Aplicación de consola en Java que lanza varios procesos externos de forma **secuencial**, espera a que cada uno termine, registra su código de salida y guarda su **salida estándar** y su **salida de error** en ficheros separados dentro de la carpeta `logs`.

Actividad de *Programación de Servicios y Procesos* (2.º DAM). No usa hilos, sockets ni contenidos de temas posteriores: solo `java.io` y `java.lang`.

## Requisitos del entorno

| Elemento | Requisito |
|---|---|
| Sistema operativo | **Windows** (los comandos usan `cmd /c`) |
| Java | JDK 8 o superior (indicar aquí la versión usada: `JDK ___`) |
| IDE | IntelliJ IDEA (indicar versión: `___`) |

> El programa **solo funciona en Windows**, porque lanza `cmd`, `type`, `ver` y un fichero `.bat`.

## Estructura del proyecto

```
GestorProcesos/
├── src/
│   └── GestorProcesos.java      Código fuente (clase principal)
├── resources/
│   └── fichero.bat              Script que se ejecuta como proceso 1
├── process/                     Directorio de trabajo del proceso 1 (se crea al ejecutar)
├── logs/                        Registros generados (se crea al ejecutar)
└── README.md
```

Contenido de `resources/fichero.bat`:

```
MKDIR NUEVO
CD NUEVO
ECHO FICHERO CREADO > hola.txt
DIR
CDDD
ECHO FIN COMANDOS
```

`CDDD` no existe a propósito: genera un mensaje en la salida de error.

## Procesos que se ejecutan

| Nº | Nombre | Comando | Tipo |
|---|---|---|---|
| 1 | `proceso1_bat` | `cmd /c <ruta>\resources\fichero.bat` (directorio de trabajo: `process`) | Correcto |
| 2 | `proceso2_ver` | `cmd /c ver` | Correcto |
| 3 | `proceso3_error_recurso` | `cmd /c type archivo_que_no_existe.txt` | Error controlado: recurso inexistente (se inicia y termina con código 1) |
| 4 | `proceso4_error_comando` | `comando_que_no_existe --opcion` | Error controlado: comando inexistente (no llega a iniciarse) |

La aplicación continúa con el siguiente proceso aunque uno falle.

## Cómo ejecutarlo

### Desde IntelliJ IDEA

1. `File > Open...` y selecciona la carpeta del proyecto.
2. Comprueba que `src` está marcada como *Sources Root* (clic derecho sobre `src > Mark Directory as > Sources Root`) y que hay un JDK configurado (`File > Project Structure > Project`).
3. Abre `src/GestorProcesos.java` y pulsa el triángulo verde junto al `main`.
4. **Directorio de trabajo:** en `Run > Edit Configurations`, el campo *Working directory* debe ser la **raíz del proyecto** (la carpeta que contiene `src`, `resources` y `logs`). Las rutas del programa son relativas a ella.

### Desde la consola de Windows

Desde la raíz del proyecto:

```
javac -d out src\GestorProcesos.java
java -cp out GestorProcesos
```

## Salida por consola

Por cada proceso se imprime una línea de resumen con el proceso lanzado, si se inició correctamente, su código de salida (cuando está disponible) y los ficheros de registro:

```
[RESUMEN] proceso1_bat [cmd, /c, ...] | Iniciado: SI | Codigo de salida: 0 | Registros: logs\...
[RESUMEN] proceso4_error_comando [comando_que_no_existe, --opcion] | Iniciado: NO | Codigo de salida: no disponible | Motivo: ...
```

## Registros generados

En `logs/` se crean dos ficheros por proceso, nunca mezclados:

```
logs/
├── proceso1_bat_salida.log          proceso1_bat_error.log
├── proceso2_ver_salida.log          proceso2_ver_error.log
├── proceso3_error_recurso_salida.log   proceso3_error_recurso_error.log
└── proceso4_error_comando_salida.log   proceso4_error_comando_error.log
```

Cada ejecución **sobrescribe** los registros anteriores.

## Notas

- `MKDIR NUEVO` falla si la carpeta `process\NUEVO` ya existe (el mensaje queda en `proceso1_bat_error.log`). Para repetir la prueba desde cero, borra `process\NUEVO`.
- El código de salida de un `.bat` es el de su **último** comando, por eso el proceso 1 termina con código 0 aunque `CDDD` falle; ese fallo se ve en su registro de error.
- Los registros se guardan con la codificación de la consola de Windows; las tildes pueden verse alteradas según el editor con el que los abras.

## Entrega

Un único `.zip` que contiene: el proyecto Java (`src`, `resources`), este `README.md`, la carpeta `logs` obtenida en las pruebas y la memoria en PDF (`memoria.pdf).