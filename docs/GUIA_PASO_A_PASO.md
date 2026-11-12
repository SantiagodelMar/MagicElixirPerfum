# Guía paso a paso: conectar Magic Elixir Perfum a XAMPP (MySQL)

Esta guía está pensada para hacerla en IntelliJ sin saber nada de bases de datos. Cada paso dice **qué hacer**, **cómo hacerlo** y **por qué se hace** (para que lo puedan explicar en la presentación).

> **Antes de empezar:** todo el código ya está escrito y compila (Java 11). Esta guía te dice cómo ponerlo en tu proyecto, cómo conectarlo y cómo explicarlo.

---

## 0. Qué se agregó (resumen de una mirada)

| Qué | Dónde | Para qué |
|---|---|---|
| Script de la base de datos | `docs/database/magic_elixir_db.sql` | Crea las 13 tablas y mete los datos de prueba |
| Conexión | `app/repository/database/DataBaseConnectionMySql.java` | Abre UNA conexión a MySQL (Singleton) |
| Mappers | `app/repository/mapper/*RowMapper.java` | Convierten una fila de la tabla en un objeto Java |
| Repositorios MySQL | `app/repository/mysql/*RepositoryMySql.java` | Escriben los `INSERT / SELECT / UPDATE / DELETE` |
| Garantías (HU-20, HU-21) | `domain/Garantia`, `GarantiaServiceImp`, `PanelGarantias`, `GarantiaView` | Registrar garantía y ver si sigue vigente |
| Reportes (HU-22, HU-23, HU-24) | `ReporteServiceImp`, `PanelReportes`, `ReporteView` | Total vendido, más vendidos, ventas por canal |
| Canal de venta (HU-24) | `domain/enums/CanalVenta` + campo en `Pedido` | Saber si la venta fue Instagram, WhatsApp o Local |
| Aprobar / rechazar devolución (HU-18) | `DevolucionServiceImp` + `PanelDevoluciones` | El administrador decide cada caso |
| `Contexto(true/false)` | `app/config/Contexto.java` | Escoge MySQL o memoria |

**Lo que NO se tocó:** los `Port` (interfaces) de los repositorios, los `View` viejos y casi todos los `Service`. Esa es justo la gracia de la arquitectura de puertos y adaptadores: se cambia *dónde* se guardan los datos sin tocar la lógica.

---

## 1. Preparar XAMPP y la base de datos

1. Abre el **XAMPP Control Panel**.
2. Pulsa **Start** en **MySQL** (Apache solo hace falta si vas a usar phpMyAdmin; también préndelo).
3. Abre en el navegador: `http://localhost/phpmyadmin`
4. Arriba, pestaña **Importar** → **Seleccionar archivo** → elige `docs/database/magic_elixir_db.sql` → baja y pulsa **Importar**.
   - (Otra opción: pestaña **SQL**, pegas todo el contenido del archivo y pulsas **Continuar**.)
5. A la izquierda debe aparecer la base **magic_elixir_db** con 13 tablas.
6. Comprueba: clic en la tabla `perfume` → deben verse **201** filas.

> El script **borra y recrea** la base cada vez. Si en la presentación quieres volver a los datos de prueba, lo importas de nuevo.

**Por qué:** las tablas son el equivalente de las listas `ArrayList` que tenías en memoria. Cada clase del dominio tiene su tabla y las **llaves foráneas (FOREIGN KEY)** son las relaciones (`Pedido` tiene un `Cliente` → `pedido.id_cliente` apunta a `cliente.id_usuario`).

Ejemplo: un pedido lleva varios perfumes (HU-10). Una columna no puede guardar una lista, por eso existe la tabla `detalle_pedido` (relación 1 a muchos).

---

## 2. Poner los archivos en tu proyecto

Hay dos formas. La **A** es la rápida; la **B** es para aprender haciéndolo tú (recomendada si el profe te va a preguntar cómo lo hicieron).

### Opción A (rápida)
1. Cierra IntelliJ.
2. Del zip final copia estas cosas **encima** de tu proyecto y acepta reemplazar: la carpeta `app/`, la carpeta `docs/` y `README.md`.
3. **No** borres ni reemplaces `.git` ni `.idea`.
4. Abre IntelliJ y sigue en el paso 3.

### Opción B (la haces tú, en este orden)
El orden importa porque cada cosa depende de la anterior (si no, IntelliJ se pone rojo):

1. Crear `domain/enums/CanalVenta.java` y agregar el campo `canalVenta` a `Pedido`.
2. Crear `domain/Garantia.java` y `domain/ItemReporte.java`.
3. Crear los puertos nuevos: `outputport/GarantiaRepositoryPort`, `inputport/GarantiaServiceInterface`, `inputport/ReporteServiceInterface`.
4. Crear `GarantiaServiceImp` y `ReporteServiceImp`; modificar `DevolucionServiceImp` y `PedidoServiceImp`.
5. Crear `repository/database/DataBaseConnectionMySql` (paso 4 de esta guía).
6. Crear `repository/mapper/RowMapper` y un mapper por clase (paso 5).
7. Crear `repository/mysql/ErroresBD` y un repositorio por clase (paso 6).
8. Modificar `Contexto` (paso 7) y `Application` (paso 8).
9. Crear `PanelGarantias`, `PanelReportes`, y modificar `PanelDevoluciones`, `PanelPedidos`, `VentanaPrincipal`.

Para cada archivo copia el contenido del zip final. Los pasos 4 a 8 de abajo explican los más importantes con su código.

---

## 3. Agregar el driver de MySQL (sin esto no conecta)

Java no sabe hablar con MySQL por sí solo; necesita un "traductor" (el **driver JDBC**). El proyecto de Luciastore lo trae con Maven; el tuyo no tiene Maven, así que se agrega a mano:

1. En IntelliJ: **File → Project Structure…** (`Ctrl+Alt+Shift+S`).
2. **Libraries** → botón **+** → **From Maven…**
3. Escribe `com.mysql:mysql-connector-j:9.7.0` (la misma versión de Luciastore) → **OK** → marca *Download to* y **OK** en todo.
4. Aplica y cierra.

Si no tienes internet en ese momento: descarga el `.jar` de *MySQL Connector/J* (mysql.com/downloads/connector/j, opción "Platform Independent"), y en **Libraries → + → Java** selecciona el `.jar`.

> Si olvidas este paso verás: `No suitable driver found for jdbc:mysql...`.

---

## 4. La conexión (patrón Singleton)

Archivo: `app/repository/database/DataBaseConnectionMySql.java`

```java
public class DataBaseConnectionMySql {

    private static DataBaseConnectionMySql instance;
    private final Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/magic_elixir_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";          // XAMPP no trae contraseña

    private DataBaseConnectionMySql() {                 // privado: nadie más puede hacer "new"
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar...", e);
        }
    }

    public static synchronized DataBaseConnectionMySql getInstance() {
        if (instance == null) {
            instance = new DataBaseConnectionMySql();
        }
        return instance;
    }

    public Connection getConnection() { return connection; }
}
```

**Cómo leer la URL:** `jdbc:mysql://` (protocolo) + `localhost:3306` (tu computador, puerto de XAMPP) + `/magic_elixir_db` (nombre de la base).

**Por qué Singleton:** abrir conexiones cuesta. Con Singleton solo hay **una** para toda la aplicación y todos los repositorios la comparten. (Es el mismo patrón de `DataBaseConnectionMySql` en Luciastore.)

Si tu XAMPP tiene contraseña para `root`, cámbiala en `PASSWORD`.

---

## 5. El Mapper (de fila a objeto)

Archivos: `RowMapper.java` (la interfaz) y un `XxxRowMapper` por clase.

```java
public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
}
```

```java
public class CategoriaRowMapper implements RowMapper<Categoria> {
    @Override
    public Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setDescripcion(rs.getString("descripcion"));
        return categoria;
    }
}
```

**Idea:** `ResultSet` es lo que devuelve un `SELECT` (una tabla). `mapRow` lee **una fila** y arma el objeto. Solo sabe hacer eso (principio **S** de SOLID: una clase, una responsabilidad). El nombre entre comillas debe ser **idéntico** al de la columna en la tabla.

**Relaciones:** `Perfume` tiene una `Categoria`, pero la tabla solo guarda `id_categoria`. El mapper crea una `Categoria` "vacía" con solo el id y el repositorio la completa con el método `hidratar(...)` (busca la categoría completa con `categoriaRepository`).

---

## 6. El Repositorio MySQL (el que habla con la base)

Archivo: `app/repository/mysql/CategoriaRepositoryMySql.java` (todos los demás son iguales con otras columnas).

```java
public class CategoriaRepositoryMySql implements CategoriaRepositoryPort {   // <- el MISMO puerto que el de memoria

    private final Connection connection;
    private final CategoriaRowMapper rowMapper;

    public CategoriaRepositoryMySql(Connection connection, CategoriaRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public Categoria createCategoria(Categoria categoria) {
        String sql = "INSERT INTO categoria (id_categoria, nombre, descripcion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoria.getIdCategoria());
            ps.setString(2, categoria.getNombre());
            ps.setString(3, categoria.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar categoria", e);
        }
        return categoria;
    }
    // selectCategoriaById, selectAllCategorias, updateCategoria y deleteCategoriaById siguen el mismo molde
}
```

**Qué significa cada parte**
- `implements CategoriaRepositoryPort`: cumple el contrato que ya existía. Por eso los services no cambian (principio **D** de SOLID: dependen de la abstracción, no de la clase concreta).
- `PreparedStatement` y los `?`: son huecos que se llenan con `setInt/setString`. Así **no** se pega texto directo al SQL y se evita la **inyección SQL**.
- `try (...)`: *try-with-resources*; cierra solo el `PreparedStatement` al terminar.
- `executeUpdate()` se usa para `INSERT/UPDATE/DELETE`; `executeQuery()` para `SELECT`.
- `ErroresBD.traducir(...)`: convierte el error técnico en un mensaje entendible (por ejemplo, "no se puede eliminar porque tiene registros relacionados").
- Si no encuentra el registro, devuelve `null` (igual que los repositorios en memoria, para que los services sigan igual).

**El caso especial: `PedidoRepositoryMySql`.** Un pedido vive en **dos tablas** (`pedido` y `detalle_pedido`). Si se guardara una y fallara la otra, quedaría un pedido sin perfumes. Por eso usa una **transacción**:

```java
connection.setAutoCommit(false);   // empieza la transacción
... INSERT en pedido ...
... INSERT de cada detalle ...
connection.commit();               // todo salió bien: se guarda TODO
// si falla algo:  connection.rollback();  -> se deshace TODO
```

---

## 7. `Contexto`: dónde se decide MySQL o memoria (patrón Simple Factory + Inyección de dependencias)

Archivo: `app/config/Contexto.java`. Es el equivalente de `Config.java` en Luciastore.

```java
public Contexto(boolean usarBaseDeDatos) {
    ...
    if (usarBaseDeDatos) {
        Connection connection = DataBaseConnectionMySql.getInstance().getConnection();
        categoriaRepository = new CategoriaRepositoryMySql(connection, new CategoriaRowMapper());
        perfumeRepository   = new PerfumeRepositoryMySql(connection, new PerfumeRowMapper(), categoriaRepository);
        ...
    } else {
        categoriaRepository = new CategoriaRepositoryImp();     // la lista en memoria de siempre
        ...
    }

    // los services reciben el repository por el constructor y NO saben cuál es
    this.categoriaService = new CategoriaServiceImp(categoriaRepository);
    ...
}
```

- `new Contexto(true)` → MySQL.  `new Contexto(false)` → memoria (con datos de prueba).
- Los datos de prueba en memoria solo se cargan con `false`; con MySQL los trae el script SQL.
- Las variables están declaradas con el tipo del **puerto** (`CategoriaRepositoryPort`), no de la clase. Esa línea es la demostración práctica del principio **D** y del **polimorfismo**.

---

## 8. Ejecutar y probar la conexión

1. Verifica que MySQL esté **verde** en XAMPP y que ya importaste el `.sql`.
2. Ejecuta `app/Application.java` (clic derecho → *Run 'Application.main()'*).
3. Inicia sesión: `admin@magicelixir.com` / `1234`.
4. Prueba que de verdad usa la base: **crea un cliente**, **cierra el programa**, ábrelo de nuevo y el cliente debe seguir ahí. También lo puedes ver en phpMyAdmin → tabla `cliente`.

Si MySQL está apagado, el programa avisa con una ventana ("No hay conexión con la base de datos") y abre en modo de prueba en memoria. **Si ves esa ventana en la presentación, NO estás conectado a la base**; revisa XAMPP.

---

## 9. Cada historia de usuario: dónde está y cómo probarla

| HU | Qué hace | Dónde está la lógica | Cómo probarla en la ventana |
|---|---|---|---|
| HU-01 | Iniciar sesión | `EmpleadoServiceImp.iniciarSesion` | Login con `admin@magicelixir.com` / `1234`; con clave mala no entra |
| HU-02 | Crear / desactivar empleados | `EmpleadoServiceImp.cambiarEstadoEmpleado` | Empleados → crear uno → botón de estado → intentar loguearse con él |
| HU-03 | Registrar clientes | `ClienteServiceImp` | Clientes → llenar y guardar |
| HU-04 | Registrar perfumes | `PerfumeServiceImp.createPerfume` | Perfumes → nuevo |
| HU-05 | Actualizar precio | `PerfumeServiceImp.actualizarPrecio` | Perfumes → seleccionar → cambiar precio |
| HU-06 | 4 categorías | tabla `categoria` | Categorías |
| HU-07 | Ver stock | `InventarioServiceImp` | Inventario (el perfume 12 sale agotado y el 5 y el 40 con bajo stock) |
| HU-08 | Aviso sin stock | `PedidoServiceImp.agregarPerfume` | Pedido 2 → agregar el perfume 12 → avisa "No hay stock" |
| HU-09 | Crear pedido | `PedidoServiceImp.createPedido` | Pedidos → crear (ahora pide **canal de venta**) |
| HU-10 | Varios perfumes en un pedido | `agregarPerfume` | Seleccionar pedido pendiente → agregar 2 o 3 perfumes |
| HU-11 | Calcula el total | `agregarPerfume` | El total se recalcula solo |
| HU-12 | Método de pago | campo `metodoPago` | Se escoge al crear el pedido |
| HU-13 | Registrar envío | `EnvioServiceImp.createEnvio` | Envíos → nuevo con guía y transportadora |
| HU-14 | Estado del envío | `actualizarEstadoEnvio` | Pasar a En camino / Entregado |
| HU-15 | Proveedores | `ProveedorServiceImp` | Proveedores |
| HU-16 | Órdenes de compra | `OrdenCompraServiceImp` | Órdenes de compra |
| HU-17 | Validar 30 días | `DevolucionServiceImp.createDevolucion` | Devoluciones → pedido 4 (entregado hace 10 días) queda *Solicitada*; pedido 6 (hace 45 días) queda *Rechazada* sola |
| HU-18 | Aprobar / rechazar | `aprobarDevolucion`, `rechazarDevolucion` | Seleccionar una *Solicitada* → botón **Aprobar** o **Rechazar** |
| HU-19 | Reembolso según motivo | `calcularReembolso` | Arrepentimiento = 80 %; los demás motivos = 100 % |
| HU-20 | Registrar garantía | `GarantiaServiceImp.createGarantia` | Garantías → nueva (pedido, fecha, meses) |
| HU-21 | ¿Sigue vigente? | `GarantiaServiceImp.estaVigente` | La garantía 1 sale *Vigente* y la 2 *Vencida*; botón **Consultar vigencia** |
| HU-22 | Total en rango de fechas | `ReporteServiceImp.totalVendido` | Reportes → fechas → Consultar |
| HU-23 | Más vendidos | `perfumesMasVendidos` | Reportes (tabla de la izquierda) |
| HU-24 | Ventas por canal | `ventasPorCanal` | Reportes (tabla de la derecha) |

**Regla de los reportes:** solo suman los pedidos **pagados, enviados o entregados** dentro del rango. Los pendientes y cancelados no cuentan.

### Guion corto para la presentación (5 minutos)
1. Mostrar XAMPP con MySQL prendido y la base en phpMyAdmin.
2. Entrar al sistema → crear un cliente → mostrar la fila nueva en phpMyAdmin (prueba de que es MySQL).
3. Crear un pedido con 2 perfumes, ver el total, y mostrar que el inventario bajó.
4. Intentar agregar el perfume agotado (HU-08).
5. Devoluciones: pedido hecho hace 45 días rechazado solo; una solicitada → Aprobar → ver el 80 %.
6. Garantías: una vigente y una vencida.
7. Reportes: total, más vendidos y por canal.
8. (Opcional, impresiona) Cambiar `new Contexto(true)` a `new Contexto(false)` en `Application` y mostrar que **solo cambia una línea** y nada más del programa.

---

## 10. Conceptos del semestre: dónde se ven en el código

| Concepto | Dónde |
|---|---|
| **Arquitectura hexagonal (puertos y adaptadores)** | `service/inputport` (lo que ofrece el service), `service/outputport` (lo que el service necesita), `repository/*` (adaptadores) |
| **S – responsabilidad única** | Un mapper solo convierte, un repository solo consulta, un service solo aplica reglas |
| **D – inversión de dependencias** | Los services reciben `...RepositoryPort` (interfaz), nunca `...RepositoryMySql` |
| **Inyección de dependencias** | Constructores de los services y repositories; el que arma todo es `Contexto` |
| **Polimorfismo** | `CategoriaRepositoryPort` puede ser `Imp` o `MySql` |
| **Singleton** | `DataBaseConnectionMySql` |
| **Simple Factory** | `Contexto` (igual que `Config` en Luciastore) |
| **Herencia** | `Usuario` → `Empleado`, `Cliente`, `Administrador`; `PanelModulo` → todos los paneles |
| **Encapsulamiento** | Atributos `private` con getters y setters |
| **Enums** | `EstadoPedido`, `MetodoDePago`, `CanalVenta`, etc. (se guardan en la base como texto con `name()`) |
| **equals y hashCode** | `Usuario`, `Categoria`, `Perfume`, `Proveedor`, `Pedido` se comparan por **id**; sin esto las listas desplegables no reconocen un objeto releído de la base |
| **Manejo de excepciones** | `try/catch` con `SQLException` → `ErroresBD` → mensaje en pantalla |
| **Transacciones** | `PedidoRepositoryMySql` (commit / rollback) |
| **Colecciones y lambdas** | `ReporteServiceImp` (Map, List, sort) |

---

## 11. Si algo falla

| Mensaje | Qué significa | Solución |
|---|---|---|
| `No suitable driver found` | Falta el driver | Paso 3 |
| `Communications link failure` / `Connection refused` | MySQL apagado o puerto distinto | Prender MySQL en XAMPP; revisar que sea el 3306 |
| `Unknown database 'magic_elixir_db'` | No importaste el `.sql` | Paso 1 |
| `Access denied for user 'root'` | `root` tiene contraseña | Poner la contraseña en `PASSWORD` de `DataBaseConnectionMySql` |
| `No se pudo guardar ...: el dato esta repetido o tiene registros relacionados` | Id o correo repetido, o borraste algo que otro registro usa (ejemplo: un cliente con pedidos) | Usar otro id, o borrar primero lo que depende de él |
| Se abre en "modo de prueba" | No hay conexión | Revisar XAMPP y el driver |
| IntelliJ marca rojo `com.mysql` | La librería no quedó agregada | Paso 3 |
| Puerto 3306 ocupado | Otro MySQL instalado | Detenerlo, o cambiar el puerto de XAMPP y de la URL |

---

## 12. Cosas que conviene saber (límites honestos)

- Los ids los digita el usuario (o el panel calcula el siguiente); la base **no** usa `AUTO_INCREMENT`. Así se respetó cómo ya trabajaba el proyecto. Con dos usuarios simultáneos podrían chocar.
- Las contraseñas de empleados están en **texto plano**, como ya estaban. En un proyecto real se guardan cifradas (hash).
- Los reportes se calculan en Java con los pedidos que entrega el repository, no con `SUM/GROUP BY` en SQL. Funciona igual en memoria y en MySQL; con muchísimos pedidos, convendría moverlos a SQL.
- Para traer un objeto con sus relaciones se hacen varias consultas pequeñas (pedido → cliente → perfumes). Con datos de un colegio/tienda pequeña no se nota.
