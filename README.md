# Sistema Generacional (Capstone)

Simulador por generaciones en Java sobre una matriz 2D que representa un ecosistema.
Cada celda puede contener vacío, árbol, animal o agua, y en cada generación se aplican
reglas de nacimiento, supervivencia y movimiento.

## Tabla de contenido

1. [Descripción general](#descripción-general)
2. [Requisitos](#requisitos)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Compilar y ejecutar](#compilar-y-ejecutar)
5. [Parámetros de entrada](#parámetros-de-entrada)
6. [Formato del mapa (`m`)](#formato-del-mapa-m)
7. [Reglas de la simulación](#reglas-de-la-simulación)
8. [Salida en consola](#salida-en-consola)
9. [Ejemplos de uso](#ejemplos-de-uso)
10. [Limitaciones y consideraciones](#limitaciones-y-consideraciones)
11. [Mejoras sugeridas](#mejoras-sugeridas)

## Descripción general

El programa se ejecuta desde línea de comandos y recibe parámetros con formato `clave=valor`.
Con esos parámetros construye un mapa inicial (aleatorio o definido por texto), imprime
la generación 0 y luego avanza por `g` generaciones aplicando reglas ecológicas.

Estados de celda:

- `0` = Vacío `[]`
- `1` = Árbol `🌲`
- `2` = Animal `🦊`
- `3` = Agua `💦`

## Requisitos

- Java JDK 8 o superior
- Terminal (PowerShell, CMD o terminal integrada del IDE)

Verificar instalación:

```bash
java -version
javac -version
```

## Estructura del proyecto

```text
Capstone/
├─ README.md
└─ src/
	 └─ Main.java
```

## Compilar y ejecutar

Desde la raíz del proyecto:

```bash
javac src/Main.java
java -cp src Main w=10 h=10 g=5 s=250 n=1 m=rnd
```

> En PowerShell los argumentos funcionan igual: `clave=valor` separados por espacios.

## Parámetros de entrada

Todos son opcionales en código, pero para una simulación útil se recomienda enviarlos todos.

| Parámetro | Descripción | Valores válidos |
|---|---|---|
| `w` | Ancho del mapa (columnas) | `5, 10, 15, 20, 40, 80` |
| `h` | Alto del mapa (filas) | `5, 10, 20, 40` |
| `g` | Cantidad de generaciones | `1..999` |
| `s` | Pausa entre generaciones (ms) | `0, 250, 500, 1000, 50000` |
| `n` | Dirección de movimiento animal | `1..4` |
| `m` | Mapa inicial | `rnd` o texto codificado |

Direcciones para `n`:

- `1` → derecha
- `2` → abajo
- `3` → izquierda
- `4` → arriba

Si un valor no es válido, el programa imprime mensajes como `w invalido`, `h invalido`, etc.

## Formato del mapa (`m`)

Puedes pasar `m=rnd` para crear un mapa aleatorio, o un mapa en texto usando:

- Números `0`, `1`, `2`, `3` para cada celda
- `#` para separar filas

Ejemplo (`w=5`, `h=4`):

```text
m=01230#10002#00310#22200
```

Notas importantes:

- Si una fila tiene menos columnas que `w`, se rellena con `0`.
- Si envías menos filas que `h`, las filas faltantes se rellenan con `0`.
- Si envías más filas/columnas, el programa usa solo lo que cabe en `h x w`.

## Reglas de la simulación

En cada generación se ejecuta este orden:

1. Nace árbol (`nacerArbol`)
2. Sobrevive/muere árbol (`arbolSobrevivir`)
3. Nace animal (`nacerAnimal`)
4. Sobrevive/muere animal (`animalSobrevivir`)
5. Nace agua (`nacerAgua`)
6. Se mueven animales (`moverAnimales`)

### Árbol

- Nace en una celda vacía si encuentra al menos 2 árboles vecinos (radio 1).
- Sobrevive solo si existe al menos una celda de agua en radio 2.

### Animal

- Solo intenta nacer en generaciones impares (según el contador interno del ciclo).
- Nace en una celda vacía si:
	- tiene al menos 2 animales en vecindad de radio 1,
	- y al menos 1 árbol en radio 2,
	- y al menos 1 agua en radio 2.
- Sobrevive solo si tiene al menos 1 árbol y 1 agua en radio 2.

### Agua

- Solo intenta nacer cuando la generación cumple múltiplo de 3.
- Se propaga verticalmente hacia abajo: si una celda vacía tiene agua arriba, se convierte en agua.

### Movimiento animal

- Cada animal intenta moverse en la dirección `n`.
- El movimiento solo ocurre si el destino está dentro del mapa.
- Puede moverse a celdas vacías (`0`) o con árbol (`1`).
- No se mueve hacia agua (`3`) ni hacia otra celda con animal (`2`).

## Salida en consola

El programa imprime:

- `Generacion 0` con el estado inicial.
- Luego `Generacion: X` para cada iteración.
- Visualización por emojis:
	- `[]` vacío
	- `🌲` árbol
	- `🦊` animal
	- `💦` agua

## Ejemplos de uso

### 1) Simulación aleatoria rápida

```bash
java -cp src Main w=10 h=10 g=10 s=250 n=1 m=rnd
```

### 2) Sin pausas, mapa definido manualmente

```bash
java -cp src Main w=5 h=5 g=8 s=0 n=4 m=01230#00120#30000#02210#00003
```

### 3) Mapa grande y movimiento a la izquierda

```bash
java -cp src Main w=20 h=20 g=15 s=500 n=3 m=rnd
```

## Limitaciones y consideraciones

- No hay interfaz gráfica; toda la salida es por consola.
- No hay persistencia de estado en archivos.
- El programa no detiene la ejecución automáticamente si faltan parámetros clave.
- Varias funciones de nacimiento/supervivencia aplican solo el primer cambio encontrado en el recorrido.
	Esto afecta la dinámica del ecosistema y puede hacerla menos “simultánea”.

---

