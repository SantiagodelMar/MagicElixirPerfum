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
- IntelliJ IDEA
- GitHub
- Git

## Organización

El proyecto se desarrolla utilizando Scrum.

## Estructura del proyecto

```
app/
├── Application.java        -> clase con el main(): inicia la consola y la tienda web
├── config/                 -> Contexto: arma repositories, services y datos iniciales una sola vez
├── domain/                 -> clases POJO
│   └── enums/              -> enums del sistema
├── repository/             -> implementacion de los repositorios (ArrayList en memoria)
├── service/                -> implementacion de los servicios
│   ├── inputport/          -> interfaces de los servicios
│   ├── outputport/         -> interfaces de los repositorios
│   ├── helpers/            -> metodos para setear los enums
│   └── validations/        -> FormTypeValidator (unico Scanner)
├── view/                   -> clases View de cada POJO
├── ui/                     -> UserInterface con los menus
└── api/                    -> API web (JSON) que usa los mismos services que la consola
web/                        -> tienda web en HTML, CSS y JavaScript (consume la API)
```

La consola (`ui/`) y la web (`api/`) son dos adaptadores de entrada sobre los mismos
services: un pedido hecho en la web aparece en el menu de pedidos de la consola, y el
estado que cambie el empleado en la consola se ve en "Mis pedidos" de la web.

## Como ejecutar

1. Abrir el proyecto en IntelliJ IDEA.
2. Ejecutar `app/Application.java`.
3. Consola: iniciar sesion con el usuario de prueba:
   - Correo: `admin@magicelixir.com`
   - Contraseña: `1234`
4. Tienda web: abrir http://localhost:8080 en el navegador e ingresar con el correo
   del cliente de prueba `anyi@gmail.com` (o crear una cuenta nueva).

Al iniciar se cargan las 4 categorias, el catalogo de perfumes con 10 unidades de cada uno, un cliente y un proveedor de prueba.

## API web

| Metodo | Ruta | Que hace |
|---|---|---|
| GET | `/api/categorias` | Las 4 categorias |
| GET | `/api/perfumes` | Catalogo con el stock del inventario |
| GET | `/api/perfumes/{id}` | Un perfume |
| GET | `/api/metodos-pago` | Opciones del enum `MetodoDePago` |
| GET | `/api/clientes?correo=...` | Ingresar con el correo del cliente |
| POST | `/api/clientes` | Registrar cliente (`nombre`, `apellido`, `correo`, `celular`, `direccionEnvio`) |
| PUT | `/api/clientes/{id}` | Actualizar `celular` y `direccionEnvio` |
| GET | `/api/clientes/{id}/pedidos` | Pedidos del cliente |
| POST | `/api/pedidos` | Crear pedido (`idCliente`, `metodoPago`, `items: [{idPerfume, cantidad}]`) |
| GET | `/api/pedidos/{id}` | Un pedido |

El servidor es el `HttpServer` que trae Java (no hay que instalar nada) y solo escucha en `localhost:8080`.
