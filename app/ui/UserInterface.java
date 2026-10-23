package ui;

import config.Contexto;
import service.validations.FormTypeValidator;
import view.AdministradorView;
import view.CategoriaView;
import view.ClienteView;
import view.DevolucionView;
import view.EmpleadoView;
import view.EnvioView;
import view.InventarioView;
import view.OrdenCompraView;
import view.PedidoView;
import view.PerfumeView;
import view.ProveedorView;

public class UserInterface {

    private final ClienteView clienteView;
    private final EmpleadoView empleadoView;
    private final AdministradorView administradorView;
    private final CategoriaView categoriaView;
    private final PerfumeView perfumeView;
    private final InventarioView inventarioView;
    private final PedidoView pedidoView;
    private final EnvioView envioView;
    private final ProveedorView proveedorView;
    private final OrdenCompraView ordenCompraView;
    private final DevolucionView devolucionView;

    public UserInterface() {
        this(new Contexto());
    }

    // Los repositories, services y datos de prueba vienen del Contexto (config/Contexto.java).
    // Es el mismo Contexto que usa la interfaz grafica, por eso los dos ven los mismos datos.
    public UserInterface(Contexto contexto) {
        // Views: se les inyecta el service por el constructor
        this.clienteView = new ClienteView(contexto.getClienteService());
        this.empleadoView = new EmpleadoView(contexto.getEmpleadoService());
        this.administradorView = new AdministradorView(contexto.getAdministradorService());
        this.categoriaView = new CategoriaView(contexto.getCategoriaService());
        this.perfumeView = new PerfumeView(contexto.getPerfumeService());
        this.inventarioView = new InventarioView(contexto.getInventarioService());
        this.pedidoView = new PedidoView(contexto.getPedidoService());
        this.envioView = new EnvioView(contexto.getEnvioService());
        this.proveedorView = new ProveedorView(contexto.getProveedorService());
        this.ordenCompraView = new OrdenCompraView(contexto.getOrdenCompraService());
        this.devolucionView = new DevolucionView(contexto.getDevolucionService());
    }

    public void menuApp() {
        System.out.println("==========================================");
        System.out.println("   SISTEMA DE GESTION - MAGIC ELIXIR PERFUM");
        System.out.println("==========================================");
        menuInicio();
    }

    // HU-01: primero se inicia sesion
    public void menuInicio() {
        int opcion;
        do {
            System.out.println("\n--- INICIO ---");
            System.out.println("1. Iniciar sesion");
            System.out.println("0. Salir");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    if (empleadoView.iniciarSesion()) {
                        menuPrincipal();
                    }
                    break;
                case 0:
                    System.out.println("Gracias por usar Magic Elixir Perfum");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Clientes");
            System.out.println("2. Empleados");
            System.out.println("3. Administradores");
            System.out.println("4. Categorias");
            System.out.println("5. Perfumes");
            System.out.println("6. Inventario");
            System.out.println("7. Pedidos");
            System.out.println("8. Envios");
            System.out.println("9. Proveedores");
            System.out.println("10. Ordenes de compra");
            System.out.println("11. Devoluciones");
            System.out.println("0. Cerrar sesion");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuEmpleados();
                    break;
                case 3:
                    menuAdministradores();
                    break;
                case 4:
                    menuCategorias();
                    break;
                case 5:
                    menuPerfumes();
                    break;
                case 6:
                    menuInventario();
                    break;
                case 7:
                    menuPedidos();
                    break;
                case 8:
                    menuEnvios();
                    break;
                case 9:
                    menuProveedores();
                    break;
                case 10:
                    menuOrdenesCompra();
                    break;
                case 11:
                    menuDevoluciones();
                    break;
                case 0:
                    System.out.println("Sesion cerrada");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuClientes() {
        int opcion;
        do {
            System.out.println("\n--- MENU CLIENTES ---");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Buscar cliente por id");
            System.out.println("3. Ver todos los clientes");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    clienteView.createCliente();
                    break;
                case 2:
                    clienteView.selectClienteById();
                    break;
                case 3:
                    clienteView.selectAllClientes();
                    break;
                case 4:
                    clienteView.updateCliente();
                    break;
                case 5:
                    clienteView.deleteCliente();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuEmpleados() {
        int opcion;
        do {
            System.out.println("\n--- MENU EMPLEADOS ---");
            System.out.println("1. Registrar empleado");
            System.out.println("2. Buscar empleado por id");
            System.out.println("3. Ver todos los empleados");
            System.out.println("4. Actualizar empleado");
            System.out.println("5. Eliminar empleado");
            System.out.println("6. Activar / desactivar cuenta");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    empleadoView.createEmpleado();
                    break;
                case 2:
                    empleadoView.selectEmpleadoById();
                    break;
                case 3:
                    empleadoView.selectAllEmpleados();
                    break;
                case 4:
                    empleadoView.updateEmpleado();
                    break;
                case 5:
                    empleadoView.deleteEmpleado();
                    break;
                case 6:
                    empleadoView.cambiarEstadoEmpleado();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuAdministradores() {
        int opcion;
        do {
            System.out.println("\n--- MENU ADMINISTRADORES ---");
            System.out.println("1. Registrar administrador");
            System.out.println("2. Buscar administrador por id");
            System.out.println("3. Ver todos los administradores");
            System.out.println("4. Actualizar administrador");
            System.out.println("5. Eliminar administrador");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    administradorView.createAdministrador();
                    break;
                case 2:
                    administradorView.selectAdministradorById();
                    break;
                case 3:
                    administradorView.selectAllAdministradores();
                    break;
                case 4:
                    administradorView.updateAdministrador();
                    break;
                case 5:
                    administradorView.deleteAdministrador();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuCategorias() {
        int opcion;
        do {
            System.out.println("\n--- MENU CATEGORIAS ---");
            System.out.println("1. Registrar categoria");
            System.out.println("2. Buscar categoria por id");
            System.out.println("3. Ver todas las categorias");
            System.out.println("4. Actualizar categoria");
            System.out.println("5. Eliminar categoria");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    categoriaView.createCategoria();
                    break;
                case 2:
                    categoriaView.selectCategoriaById();
                    break;
                case 3:
                    categoriaView.selectAllCategorias();
                    break;
                case 4:
                    categoriaView.updateCategoria();
                    break;
                case 5:
                    categoriaView.deleteCategoria();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuPerfumes() {
        int opcion;
        do {
            System.out.println("\n--- MENU PERFUMES ---");
            System.out.println("1. Registrar perfume");
            System.out.println("2. Buscar perfume por id");
            System.out.println("3. Ver todo el catalogo");
            System.out.println("4. Ver perfumes por categoria");
            System.out.println("5. Actualizar perfume");
            System.out.println("6. Actualizar precio");
            System.out.println("7. Eliminar perfume");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    perfumeView.createPerfume();
                    break;
                case 2:
                    perfumeView.selectPerfumeById();
                    break;
                case 3:
                    perfumeView.selectAllPerfumes();
                    break;
                case 4:
                    perfumeView.selectPerfumesByCategoria();
                    break;
                case 5:
                    perfumeView.updatePerfume();
                    break;
                case 6:
                    perfumeView.actualizarPrecio();
                    break;
                case 7:
                    perfumeView.deletePerfume();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuInventario() {
        int opcion;
        do {
            System.out.println("\n--- MENU INVENTARIO ---");
            System.out.println("1. Registrar inventario de un perfume");
            System.out.println("2. Buscar inventario por id");
            System.out.println("3. Consultar stock de un perfume");
            System.out.println("4. Ver todo el inventario");
            System.out.println("5. Actualizar stock");
            System.out.println("6. Eliminar inventario");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    inventarioView.createInventario();
                    break;
                case 2:
                    inventarioView.selectInventarioById();
                    break;
                case 3:
                    inventarioView.consultarStockPerfume();
                    break;
                case 4:
                    inventarioView.selectAllInventarios();
                    break;
                case 5:
                    inventarioView.updateStock();
                    break;
                case 6:
                    inventarioView.deleteInventario();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuPedidos() {
        int opcion;
        do {
            System.out.println("\n--- MENU PEDIDOS ---");
            System.out.println("1. Crear pedido");
            System.out.println("2. Agregar perfume a un pedido");
            System.out.println("3. Buscar pedido por id");
            System.out.println("4. Ver todos los pedidos");
            System.out.println("5. Actualizar pedido");
            System.out.println("6. Cambiar estado del pedido");
            System.out.println("7. Eliminar pedido");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    pedidoView.createPedido();
                    break;
                case 2:
                    pedidoView.agregarPerfumeAPedido();
                    break;
                case 3:
                    pedidoView.selectPedidoById();
                    break;
                case 4:
                    pedidoView.selectAllPedidos();
                    break;
                case 5:
                    pedidoView.updatePedido();
                    break;
                case 6:
                    pedidoView.cambiarEstadoPedido();
                    break;
                case 7:
                    pedidoView.deletePedido();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuEnvios() {
        int opcion;
        do {
            System.out.println("\n--- MENU ENVIOS ---");
            System.out.println("1. Registrar envio");
            System.out.println("2. Buscar envio por id");
            System.out.println("3. Rastrear por numero de guia");
            System.out.println("4. Ver todos los envios");
            System.out.println("5. Actualizar envio");
            System.out.println("6. Actualizar estado del envio");
            System.out.println("7. Eliminar envio");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    envioView.createEnvio();
                    break;
                case 2:
                    envioView.selectEnvioById();
                    break;
                case 3:
                    envioView.rastrearEnvio();
                    break;
                case 4:
                    envioView.selectAllEnvios();
                    break;
                case 5:
                    envioView.updateEnvio();
                    break;
                case 6:
                    envioView.actualizarEstadoEnvio();
                    break;
                case 7:
                    envioView.deleteEnvio();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuProveedores() {
        int opcion;
        do {
            System.out.println("\n--- MENU PROVEEDORES ---");
            System.out.println("1. Registrar proveedor");
            System.out.println("2. Buscar proveedor por id");
            System.out.println("3. Ver todos los proveedores");
            System.out.println("4. Actualizar proveedor");
            System.out.println("5. Activar / desactivar proveedor");
            System.out.println("6. Eliminar proveedor");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    proveedorView.createProveedor();
                    break;
                case 2:
                    proveedorView.selectProveedorById();
                    break;
                case 3:
                    proveedorView.selectAllProveedores();
                    break;
                case 4:
                    proveedorView.updateProveedor();
                    break;
                case 5:
                    proveedorView.cambiarEstadoProveedor();
                    break;
                case 6:
                    proveedorView.deleteProveedor();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuOrdenesCompra() {
        int opcion;
        do {
            System.out.println("\n--- MENU ORDENES DE COMPRA ---");
            System.out.println("1. Crear orden de compra");
            System.out.println("2. Buscar orden por id");
            System.out.println("3. Ver todas las ordenes");
            System.out.println("4. Actualizar orden");
            System.out.println("5. Cambiar estado de la orden");
            System.out.println("6. Eliminar orden");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    ordenCompraView.createOrdenCompra();
                    break;
                case 2:
                    ordenCompraView.selectOrdenCompraById();
                    break;
                case 3:
                    ordenCompraView.selectAllOrdenesCompra();
                    break;
                case 4:
                    ordenCompraView.updateOrdenCompra();
                    break;
                case 5:
                    ordenCompraView.cambiarEstadoOrdenCompra();
                    break;
                case 6:
                    ordenCompraView.deleteOrdenCompra();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

    public void menuDevoluciones() {
        int opcion;
        do {
            System.out.println("\n--- MENU DEVOLUCIONES ---");
            System.out.println("1. Registrar devolucion");
            System.out.println("2. Buscar devolucion por id");
            System.out.println("3. Ver todas las devoluciones");
            System.out.println("4. Actualizar devolucion");
            System.out.println("5. Eliminar devolucion");
            System.out.println("6. Registrar reembolso");
            System.out.println("0. Volver");
            opcion = FormTypeValidator.intValidator("Seleccione una opcion:");

            switch (opcion) {
                case 1:
                    devolucionView.createDevolucion();
                    break;
                case 2:
                    devolucionView.selectDevolucionById();
                    break;
                case 3:
                    devolucionView.selectAllDevoluciones();
                    break;
                case 4:
                    devolucionView.updateDevolucion();
                    break;
                case 5:
                    devolucionView.deleteDevolucion();
                    break;
                case 6:
                    devolucionView.registrarReembolso();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion != 0);
    }

}
