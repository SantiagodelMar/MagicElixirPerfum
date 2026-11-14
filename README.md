# Magic Elixir Perfum

Sistema de gestión de ventas, inventario y logística para la perfumería Magic Elixir Perfum.

## Equipo

- Juan Manuel Mira Janne
- Aaron Mateo Ruiz (PO)
- Anyi Paola Cañas Suaza (SM)
- Feliciano Escobar
- Harry Uran Molina
- Santiago Rodríguez Marín

## Tecnologías

- Java 11
- Java Swing (interfaz gráfica)
- MySQL en XAMPP + JDBC (mysql-connector-j)
- IntelliJ IDEA
- GitHub
- Git

## Organización

El proyecto se desarrolla utilizando Scrum.

## Estructura del proyecto

```
app/
├── Application.java        -> main() de la interfaz gráfica (ventanas)
├── ApplicationConsola.java -> main() de la versión por consola (CLI)
├── config/                 -> Contexto: arma repositories, services y datos de prueba
├── domain/                 -> clases POJO
│   └── enums/              -> enums del sistema
├── repository/             -> implementacion de los repositorios en memoria (ArrayList) y las capas de MySQL
│   ├── database/           -> DataBaseConnectionMySql (Singleton: una sola conexion)
│   ├── mapper/             -> RowMapper: convierte una fila de la base en un objeto
│   └── mysql/              -> *RepositoryMySql: los SQL (INSERT, SELECT, UPDATE, DELETE)
├── service/                -> implementacion de los servicios
│   ├── inputport/          -> interfaces de los servicios
│   ├── outputport/         -> interfaces de los repositorios
│   ├── helpers/            -> metodos para setear los enums
│   └── validations/        -> FormTypeValidator (unico Scanner)
├── view/                   -> clases View de cada POJO (consola)
├── ui/                     -> UserInterface con los menus (consola)
├── gui/                    -> interfaz gráfica (front)
│   ├── VentanaLogin.java       -> inicio de sesión (HU-01)
│   ├── VentanaPrincipal.java   -> menú lateral + pantallas con CardLayout
│   ├── paneles/                -> una pantalla por módulo (PanelClientes, PanelPedidos...)
│   ├── componentes/            -> botones, campos, tablas y tarjetas con el estilo de la marca
│   ├── estilo/                 -> colores, fuentes y formatos ($ 240.000, fechas)
│   └── validaciones/           -> ValidadorCampos (el FormTypeValidator de las ventanas)
└── recursos/               -> logo, fuentes (Playfair Display e Inter) y fotos de los perfumes
```

## Cómo se conecta el front con el back

La interfaz gráfica **no repite lógica**: cada pantalla recibe por el constructor los
`service/inputport` que necesita (inyección de dependencias), igual que los View de la consola.

```
VentanaPrincipal ──> PanelPedidos ──> PedidoServiceInterface ──> PedidoServiceImp ──> PedidoRepositoryPort ──> ArrayList
UserInterface    ──> PedidoView   ──┘   (los dos usan el mismo service)
```

El `PedidoRepositoryPort` lo implementan dos clases: `PedidoRepositoryImp` (ArrayList) y
`PedidoRepositoryMySql` (base de datos). `config/Contexto` decide cuál se usa; los services no cambian.

## Base de datos (XAMPP)

1. Prender **MySQL** en el panel de XAMPP.
2. En phpMyAdmin (http://localhost/phpmyadmin) importar `docs/database/magic_elixir_db.sql`.
3. Agregar el driver al proyecto: `com.mysql:mysql-connector-j:9.7.0` (File > Project Structure > Libraries > From Maven).
4. Ejecutar `Application.java`. Usuario de prueba: `admin@magicelixir.com` / `1234`.

La guía completa paso a paso está en `docs/GUIA_PASO_A_PASO.md`.

Las reglas de negocio (stock, total del pedido, plazo de 30 días de las devoluciones,
proveedores activos, etc.) están en un solo lugar: los services.

## Pantallas

| Pantalla | Historias de usuario |
|---|---|
| Inicio de sesión | HU-01 |
| Inicio (resumen) | HU-06, HU-07 |
| Clientes | HU-03 |
| Empleados | HU-01, HU-02 |
| Administradores | — |
| Vitrina (catálogo con fotos) | HU-04, HU-06, HU-07 |
| Categorías | HU-06 |
| Perfumes | HU-04, HU-05, HU-06 |
| Inventario | HU-07 |
| Pedidos | HU-08, HU-09, HU-10, HU-11, HU-12 |
| Envíos | HU-13, HU-14 |
| Devoluciones | HU-17, HU-18, HU-19 |
| Garantías | HU-20, HU-21 |
| Proveedores | HU-15 |
| Órdenes de compra | HU-16 |
| Reportes (Ventas e indicadores) | HU-22, HU-23, HU-24 |

## Fotos de los perfumes

La vitrina muestra una foto por perfume. Las fotos están en `app/recursos/img/perfumes`
y se llaman con la marca y el nombre en minúscula, sin tildes y con guiones:

```
Lattafa + Khamrah Qahwa  ->  lattafa-khamrah-qahwa.png
```

Por ahora son ilustraciones del frasco con las iniciales del perfume. Para poner la foto
real basta con guardarla (PNG, idealmente 300 x 375) con el mismo nombre.
Si un perfume nuevo no tiene foto, se muestra `perfume-generico.png`.

## Como ejecutar

1. Abrir el proyecto en IntelliJ IDEA (y dejar prendido MySQL en XAMPP, ver la sección anterior).
2. Ejecutar `app/Application.java` para abrir la interfaz gráfica.
   (Para la versión por consola ejecutar `app/ApplicationConsola.java`).
3. Iniciar sesion con el usuario de prueba:
   - Correo: `admin@magicelixir.com`
   - Contraseña: `1234`

Con la base de datos, esos datos de prueba vienen del script `docs/database/magic_elixir_db.sql`.
Si MySQL no está prendido, el programa avisa y abre en modo de prueba (memoria) cargando:
las 4 categorias, el catalogo de perfumes con 10 unidades de cada uno,
clientes, empleados, proveedores y algunos pedidos, envíos, órdenes de compra y una devolución
de prueba para que las pantallas no salgan vacías.
