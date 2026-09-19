# Sprint 1 — Sprint Backlog

## Objetivo del Sprint
Establecer la estructura base del proyecto y definir el Product Backlog completo con épicas, features e historias de usuario.

## Historias seleccionadas para Sprint 1

| ID | Historia | Responsable | Estado |
|---|---|---|---|
| HU-01 | Como empleado, quiero iniciar sesión con correo y contraseña | Juan Manuel | In Progress |
| HU-03 | Como vendedor, quiero registrar clientes | Aaron | In Progress |
| HU-04 | Como administrador, quiero registrar perfumes | Aaron | In Progress |
| HU-07 | Como administrador, quiero ver cantidad disponible | Aaron | In Progress |
| HU-09 | Como vendedor, quiero crear un pedido | Anyi | In Progress |
| HU-13 | Como vendedor, quiero registrar envío | Feliciano | In Progress |
| HU-17 | Como vendedor, quiero validar devoluciones en plazo | Santiago | In Progress |

## Criterios de aceptación por historia

### HU-17
- Dado un pedido entregado hace 10 días, cuando se solicita devolución, entonces el sistema la acepta.
- Dado un pedido entregado hace 45 días, cuando se solicita devolución, entonces el sistema la rechaza.

## Notas
- Comenzó el 19 de septiembre de 2026
- Estructura del repositorio reorganizada siguiendo Scrum + GitHub

## Criterios de aceptación detallados

### HU-03 — Registrar cliente
✓ Acepta nombre, teléfono, correo válido
✓ Guarda dirección (calle, barrio, ciudad, departamento)
✓ Guarda usuario de Instagram
✓ No permite duplicar teléfono
✓ Muestra confirmación de cliente guardado

### HU-04 — Registrar perfume
✓ Solicita: nombre, marca, mililitros, precio, notas olfativas
✓ Asigna categoría de lista (femenina, masculina, árabe fem, árabe masc)
✓ Genera código único automático
✓ Valida que precio sea positivo
✓ Guarda el perfume en el sistema
✓ Muestra confirmación

### HU-07 — Ver cantidad disponible
✓ Muestra lista de perfumes con stock actual
✓ Actualiza automáticamente después de cada venta
✓ Marca en rojo si stock es 0
✓ Marca en amarillo si stock es bajo (< 3 unidades)
✓ Permite filtrar por categoría

### HU-09 — Crear pedido
✓ Selecciona cliente (existente o nuevo)
✓ Registra fecha y hora del pedido
✓ Asigna ID único al pedido
✓ Registra al vendedor que lo crea
✓ Estado inicial: PENDIENTE
✓ Guarda el pedido

### HU-13 — Registrar envío
✓ Selecciona transportadora (Coordinadora, Envía, etc.)
✓ Ingresa número de guía
✓ Confirma dirección de destino del cliente
✓ Registra fecha de envío
✓ Estado inicial del envío: EN PREPARACIÓN
✓ Genera confirmación con detalles del envío
