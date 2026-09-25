\# Sprint 1 — Sprint Backlog



\## Objetivo del Sprint



Construir las funciones base del sistema: inicio de sesión, registro de clientes y perfumes, consulta de inventario, creación de pedidos, registro de envíos y validación de devoluciones.



\## Historias seleccionadas



| ID | Historia | Responsable | Estado |

|---|---|---|---|

| HU-01 | Como empleado, quiero iniciar sesión con correo y contraseña | Juan Manuel | En progreso |

| HU-03 | Como vendedor, quiero registrar clientes | Juan Manuel | En progreso |

| HU-04 | Como administrador, quiero registrar perfumes | Aaron | En progreso |

| HU-07 | Como administrador, quiero ver la cantidad disponible | Harry | En progreso |

| HU-09 | Como vendedor, quiero crear un pedido | Anyi | En progreso |

| HU-13 | Como vendedor, quiero registrar un envío | Feliciano | En progreso |

| HU-17 | Como vendedor, quiero validar que la devolución esté en plazo | Santiago | En progreso |



\## Criterios de aceptación



\### Historia Usuario-01 — Iniciar sesión



\- Dado que ingresa correo y contraseña válidos, cuando presiona "Iniciar sesión", entonces accede al panel principal.

\- Dado que ingresa datos inválidos, cuando intenta iniciar sesión, entonces ve el mensaje "Correo o contraseña incorrectos".

\- Dado que deja campos sin llenar, cuando intenta enviar el formulario, entonces el sistema le exige completarlos.



\### Historia Usuario-03 — Registrar cliente



\- El sistema permite ingresar los datos del cliente (nombre, identificación y contacto).

\- Se impide registrar un cliente con una identificación ya existente.

\- El sistema muestra un mensaje de éxito tras guardarse la información.



\### Historia Usuario-04 — Registrar perfume



\- El sistema permite ingresar nombre, categoría, precio y stock inicial del perfume.

\- Se valida que ningún campo quede vacío y que el precio/stock sean números mayores a cero.

\- El perfume registrado queda habilitado en el catálogo.



\### Historia Usuario-07 — Ver cantidad disponible



\- Muestra en pantalla las unidades disponibles del perfume seleccionado.

\- Muestra una alerta visual si el stock es igual o menor al límite mínimo.

\- Refleja los cambios de inventario inmediatamente tras cada venta o registro.



\### Historia Usuario-09 — Crear pedido



\- Pendiente (Anyi).



\### Historia Usuario-13 — Registrar envío



\- Pendiente (Feliciano).



\### Historia Usuario -17 — Validar devolución en plazo



\- Dado un pedido entregado hace 10 días, cuando se solicita la devolución, entonces el sistema la acepta.

\- Dado un pedido entregado hace 45 días, cuando se solicita la devolución, entonces el sistema la rechaza.

