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
├── repository/             -> implementacion de los repositorios (ArrayList en memoria)
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
└── recursos/               -> logo y fuentes (Playfair Display e Inter)
```

## Cómo se conecta el front con el back

La interfaz gráfica **no repite lógica**: cada pantalla recibe por el constructor los
`service/inputport` que necesita (inyección de dependencias), igual que los View de la consola.

```
VentanaPrincipal ──> PanelPedidos ──> PedidoServiceInterface ──> PedidoServiceImp ──> PedidoRepositoryPort ──> ArrayList
UserInterface    ──> PedidoView   ──┘   (los dos usan el mismo service)
```

Por eso las reglas de negocio (stock, total del pedido, plazo de 30 días de las devoluciones,
proveedores activos, etc.) están en un solo lugar: los services.

## Pantallas

| Pantalla | Historias de usuario |
|---|---|
| Inicio de sesión | HU-01 |
| Inicio (resumen) | HU-06, HU-07 |
| Clientes | HU-03 |
| Empleados | HU-01, HU-02 |
| Administradores | — |
| Categorías | HU-06 |
| Perfumes | HU-04, HU-05, HU-06 |
| Inventario | HU-07 |
| Pedidos | HU-08, HU-09, HU-10, HU-11, HU-12 |
| Envíos | HU-13, HU-14 |
| Devoluciones | HU-17, HU-19 |
| Proveedores | HU-15 |
| Órdenes de compra | HU-16 |

## Como ejecutar

1. Abrir el proyecto en IntelliJ IDEA.
2. Ejecutar `app/Application.java` para abrir la interfaz gráfica.
   (Para la versión por consola ejecutar `app/ApplicationConsola.java`).
3. Iniciar sesion con el usuario de prueba:
   - Correo: `admin@magicelixir.com`
   - Contraseña: `1234`

Al iniciar se cargan las 4 categorias, el catalogo de perfumes con 10 unidades de cada uno,
clientes, empleados, proveedores y algunos pedidos, envíos, órdenes de compra y una devolución
de prueba para que las pantallas no salgan vacías.
