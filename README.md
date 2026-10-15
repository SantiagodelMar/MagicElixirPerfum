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
├── Application.java        -> clase con el main()
├── domain/                 -> clases POJO
│   └── enums/              -> enums del sistema
├── repository/             -> implementacion de los repositorios (ArrayList en memoria)
├── service/                -> implementacion de los servicios
│   ├── inputport/          -> interfaces de los servicios
│   ├── outputport/         -> interfaces de los repositorios
│   ├── helpers/            -> metodos para setear los enums
│   └── validations/        -> FormTypeValidator (unico Scanner)
├── view/                   -> clases View de cada POJO
└── ui/                     -> UserInterface con los menus
```

## Como ejecutar

1. Abrir el proyecto en IntelliJ IDEA.
2. Ejecutar `app/Application.java`.
3. Iniciar sesion con el usuario de prueba:
   - Correo: `admin@magicelixir.com`
   - Contraseña: `1234`

Al iniciar se cargan las 4 categorias, el catalogo de perfumes con 10 unidades de cada uno, un cliente y un proveedor de prueba.
