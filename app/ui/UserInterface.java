package ui;

import domain.Categoria;
import domain.Perfume;
import domain.enums.TipoDocumento;
import repository.AdministradorRepositoryImp;
import repository.CargadorDatos;
import repository.CategoriaRepositoryImp;
import repository.ClienteRepositoryImp;
import repository.DevolucionRepositoryImp;
import repository.EmpleadoRepositoryImp;
import repository.EnvioRepositoryImp;
import repository.InventarioRepositoryImp;
import repository.OrdenCompraRepositoryImp;
import repository.PedidoRepositoryImp;
import repository.PerfumeRepositoryImp;
import repository.ProveedorRepositoryImp;
import service.AdministradorServiceImp;
import service.CategoriaServiceImp;
import service.ClienteServiceImp;
import service.DevolucionServiceImp;
import service.EmpleadoServiceImp;
import service.EnvioServiceImp;
import service.InventarioServiceImp;
import service.OrdenCompraServiceImp;
import service.PedidoServiceImp;
import service.PerfumeServiceImp;
import service.ProveedorServiceImp;
import service.inputport.AdministradorServiceInterface;
import service.inputport.CategoriaServiceInterface;
import service.inputport.ClienteServiceInterface;
import service.inputport.DevolucionServiceInterface;
import service.inputport.EmpleadoServiceInterface;
import service.inputport.EnvioServiceInterface;
import service.inputport.InventarioServiceInterface;
import service.inputport.OrdenCompraServiceInterface;
import service.inputport.PedidoServiceInterface;
import service.inputport.PerfumeServiceInterface;
import service.inputport.ProveedorServiceInterface;
import service.outputport.AdministradorRepositoryPort;
import service.outputport.CategoriaRepositoryPort;
import service.outputport.ClienteRepositoryPort;
import service.outputport.DevolucionRepositoryPort;
import service.outputport.EmpleadoRepositoryPort;
import service.outputport.EnvioRepositoryPort;
import service.outputport.InventarioRepositoryPort;
import service.outputport.OrdenCompraRepositoryPort;
import service.outputport.PedidoRepositoryPort;
import service.outputport.PerfumeRepositoryPort;
import service.outputport.ProveedorRepositoryPort;
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

import java.util.List;

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
        // 1. Repositories (las listas en memoria)
        ClienteRepositoryPort clienteRepository = new ClienteRepositoryImp();
        EmpleadoRepositoryPort empleadoRepository = new EmpleadoRepositoryImp();
        AdministradorRepositoryPort administradorRepository = new AdministradorRepositoryImp();
        CategoriaRepositoryPort categoriaRepository = new CategoriaRepositoryImp();
        PerfumeRepositoryPort perfumeRepository = new PerfumeRepositoryImp();
        InventarioRepositoryPort inventarioRepository = new InventarioRepositoryImp();
        PedidoRepositoryPort pedidoRepository = new PedidoRepositoryImp();
        EnvioRepositoryPort envioRepository = new EnvioRepositoryImp();
        ProveedorRepositoryPort proveedorRepository = new ProveedorRepositoryImp();
        OrdenCompraRepositoryPort ordenCompraRepository = new OrdenCompraRepositoryImp();
        DevolucionRepositoryPort devolucionRepository = new DevolucionRepositoryImp();

        // 2. Services: se les inyecta el repository por el constructor
        ClienteServiceInterface clienteService = new ClienteServiceImp(clienteRepository);
        EmpleadoServiceInterface empleadoService = new EmpleadoServiceImp(empleadoRepository);
        AdministradorServiceInterface administradorService = new AdministradorServiceImp(administradorRepository);
        CategoriaServiceInterface categoriaService = new CategoriaServiceImp(categoriaRepository);
        PerfumeServiceInterface perfumeService = new PerfumeServiceImp(perfumeRepository, categoriaRepository);
        InventarioServiceInterface inventarioService = new InventarioServiceImp(inventarioRepository, perfumeRepository);
        PedidoServiceInterface pedidoService = new PedidoServiceImp(pedidoRepository, clienteRepository, perfumeRepository, inventarioRepository);
        EnvioServiceInterface envioService = new EnvioServiceImp(envioRepository, pedidoRepository);
        ProveedorServiceInterface proveedorService = new ProveedorServiceImp(proveedorRepository);
        OrdenCompraServiceInterface ordenCompraService = new OrdenCompraServiceImp(ordenCompraRepository, proveedorRepository);
        DevolucionServiceInterface devolucionService = new DevolucionServiceImp(devolucionRepository);

        // 3. Views: se les inyecta el service por el constructor
        this.clienteView = new ClienteView(clienteService);
        this.empleadoView = new EmpleadoView(empleadoService);
        this.administradorView = new AdministradorView(administradorService);
        this.categoriaView = new CategoriaView(categoriaService);
        this.perfumeView = new PerfumeView(perfumeService);
        this.inventarioView = new InventarioView(inventarioService);
        this.pedidoView = new PedidoView(pedidoService);
        this.envioView = new EnvioView(envioService);
        this.proveedorView = new ProveedorView(proveedorService);
        this.ordenCompraView = new OrdenCompraView(ordenCompraService);
        this.devolucionView = new DevolucionView(devolucionService);

        cargarDatosIniciales(empleadoService, administradorService, clienteService, categoriaService,
                perfumeService, inventarioService, proveedorService);
    }

    // Datos de prueba para no empezar con todo vacio
    private void cargarDatosIniciales(EmpleadoServiceInterface empleadoService, AdministradorServiceInterface administradorService,
                                      ClienteServiceInterface clienteService, CategoriaServiceInterface categoriaService,
                                      PerfumeServiceInterface perfumeService, InventarioServiceInterface inventarioService,
                                      ProveedorServiceInterface proveedorService) {

        empleadoService.createEmpleado(1, "Admin", "Magic Elixir", "admin@magicelixir.com", TipoDocumento.CC,
                "3000000000", "Medellin", "2000-01-01", "Vendedor", "1234");

        administradorService.createAdministrador(1, "Aaron Mateo", "Ruiz", "aaron@magicelixir.com", TipoDocumento.CC,
                "3001112233", "Medellin", "2000-05-10", "Total", "1");

        clienteService.createCliente(1, "Anyi", "Cañas", "anyi@gmail.com", TipoDocumento.CC, "3001234567",
                "Calle 10 #20-30", "2001-03-15", "Frecuente", "Calle 10 #20-30");

        proveedorService.createProveedor(1, "Distribuidora Arabe SAS", "900123456", "6044445566",
                "ventas@distriarabe.com", "Bogota");

        // HU-06: las 4 categorias de Instagram (en el orden que usa CargadorDatos)
        categoriaService.createCategoria(1, "Árabe Masculina", "Fragancias arabes para hombre");
        categoriaService.createCategoria(2, "Perfumería Masculina", "Fragancias de diseñador para hombre");
        categoriaService.createCategoria(3, "Árabe Femenina", "Fragancias arabes para mujer");
        categoriaService.createCategoria(4, "Perfumería Femenina", "Fragancias de diseñador para mujer");

        // Catalogo completo de perfumes y 10 unidades de cada uno
        List<Categoria> categorias = categoriaService.selectAllCategorias();
        List<Perfume> catalogo = CargadorDatos.cargarPerfumes(categorias);

        for (Perfume p : catalogo) {
            perfumeService.createPerfume(p.getIdPerfume(), p.getNombre(), p.getMarca(), p.getPrecio(),
                    p.getMililitros(), p.getNotasOlfativas(), p.getCategoria().getIdCategoria());
            inventarioService.createInventario(p.getIdPerfume(), p.getIdPerfume(), 10);
        }
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
