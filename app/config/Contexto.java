package config;

import domain.Categoria;
import domain.Envio;
import domain.Pedido;
import domain.Perfume;
import domain.enums.CanalVenta;
import domain.enums.EstadoEnvio;
import domain.enums.EstadoOrdenCompra;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;
import domain.enums.MotivoDevolucion;
import domain.enums.TipoDocumento;
import repository.AdministradorRepositoryImp;
import repository.GarantiaRepositoryImp;
import repository.database.DataBaseConnectionMySql;
import repository.mapper.*;
import repository.mysql.*;
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
import service.GarantiaServiceImp;
import service.ReporteServiceImp;
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
import service.inputport.GarantiaServiceInterface;
import service.inputport.ReporteServiceInterface;
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
import service.outputport.GarantiaRepositoryPort;
import service.outputport.InventarioRepositoryPort;
import service.outputport.OrdenCompraRepositoryPort;
import service.outputport.PedidoRepositoryPort;
import service.outputport.PerfumeRepositoryPort;
import service.outputport.ProveedorRepositoryPort;

import java.sql.Connection;
import java.util.List;

// Aqui se arman los repositories y los services UNA sola vez (es la "fabrica" de objetos).
// Lo usan la consola (ui/UserInterface) y la interfaz grafica (gui/VentanaPrincipal).
// Hay dos modos:
//   - new Contexto(true)  -> los repositories son los de MySQL (XAMPP) y los datos viven en la base
//   - new Contexto(false) -> los repositories son listas en memoria con datos de prueba
// Los services NO se enteran del cambio porque solo conocen la interfaz (Port): principio D de SOLID.
public class Contexto {

    //Atributos (se guardan las abstracciones, no las implementaciones -> principio D de SOLID)

    private final ClienteServiceInterface clienteService;
    private final EmpleadoServiceInterface empleadoService;
    private final AdministradorServiceInterface administradorService;
    private final CategoriaServiceInterface categoriaService;
    private final PerfumeServiceInterface perfumeService;
    private final InventarioServiceInterface inventarioService;
    private final PedidoServiceInterface pedidoService;
    private final EnvioServiceInterface envioService;
    private final ProveedorServiceInterface proveedorService;
    private final OrdenCompraServiceInterface ordenCompraService;
    private final DevolucionServiceInterface devolucionService;
    private final GarantiaServiceInterface garantiaService;
    private final ReporteServiceInterface reporteService;
    private final boolean usandoBaseDeDatos;

    //Constructor

    // Sin parametros: modo memoria (como estaba antes de tener base de datos)
    public Contexto() {
        this(false);
    }

    public Contexto(boolean usarBaseDeDatos) {
        this.usandoBaseDeDatos = usarBaseDeDatos;

        // 1. Repositories: se escoge la implementacion segun el modo
        ClienteRepositoryPort clienteRepository;
        EmpleadoRepositoryPort empleadoRepository;
        AdministradorRepositoryPort administradorRepository;
        CategoriaRepositoryPort categoriaRepository;
        PerfumeRepositoryPort perfumeRepository;
        InventarioRepositoryPort inventarioRepository;
        PedidoRepositoryPort pedidoRepository;
        EnvioRepositoryPort envioRepository;
        ProveedorRepositoryPort proveedorRepository;
        OrdenCompraRepositoryPort ordenCompraRepository;
        DevolucionRepositoryPort devolucionRepository;
        GarantiaRepositoryPort garantiaRepository;

        if (usarBaseDeDatos) {
            // Una sola conexion (Singleton) que se le pasa a todos los repositories
            Connection connection = DataBaseConnectionMySql.getInstance().getConnection();

            clienteRepository = new ClienteRepositoryMySql(connection, new ClienteRowMapper());
            empleadoRepository = new EmpleadoRepositoryMySql(connection, new EmpleadoRowMapper());
            administradorRepository = new AdministradorRepositoryMySql(connection, new AdministradorRowMapper());
            categoriaRepository = new CategoriaRepositoryMySql(connection, new CategoriaRowMapper());
            perfumeRepository = new PerfumeRepositoryMySql(connection, new PerfumeRowMapper(), categoriaRepository);
            inventarioRepository = new InventarioRepositoryMySql(connection, new InventarioRowMapper(), perfumeRepository);
            pedidoRepository = new PedidoRepositoryMySql(connection, new PedidoRowMapper(), clienteRepository, perfumeRepository);
            envioRepository = new EnvioRepositoryMySql(connection, new EnvioRowMapper(), pedidoRepository);
            proveedorRepository = new ProveedorRepositoryMySql(connection, new ProveedorRowMapper());
            ordenCompraRepository = new OrdenCompraRepositoryMySql(connection, new OrdenCompraRowMapper(), proveedorRepository);
            devolucionRepository = new DevolucionRepositoryMySql(connection, new DevolucionRowMapper());
            garantiaRepository = new GarantiaRepositoryMySql(connection, new GarantiaRowMapper(), pedidoRepository);
        } else {
            clienteRepository = new ClienteRepositoryImp();
            empleadoRepository = new EmpleadoRepositoryImp();
            administradorRepository = new AdministradorRepositoryImp();
            categoriaRepository = new CategoriaRepositoryImp();
            perfumeRepository = new PerfumeRepositoryImp();
            inventarioRepository = new InventarioRepositoryImp();
            pedidoRepository = new PedidoRepositoryImp();
            envioRepository = new EnvioRepositoryImp();
            proveedorRepository = new ProveedorRepositoryImp();
            ordenCompraRepository = new OrdenCompraRepositoryImp();
            devolucionRepository = new DevolucionRepositoryImp();
            garantiaRepository = new GarantiaRepositoryImp();
        }

        // 2. Services: se les inyecta el repository por el constructor
        this.clienteService = new ClienteServiceImp(clienteRepository);
        this.empleadoService = new EmpleadoServiceImp(empleadoRepository);
        this.administradorService = new AdministradorServiceImp(administradorRepository);
        this.categoriaService = new CategoriaServiceImp(categoriaRepository);
        this.perfumeService = new PerfumeServiceImp(perfumeRepository, categoriaRepository);
        this.inventarioService = new InventarioServiceImp(inventarioRepository, perfumeRepository);
        this.pedidoService = new PedidoServiceImp(pedidoRepository, clienteRepository, perfumeRepository, inventarioRepository);
        this.envioService = new EnvioServiceImp(envioRepository, pedidoRepository);
        this.proveedorService = new ProveedorServiceImp(proveedorRepository);
        this.ordenCompraService = new OrdenCompraServiceImp(ordenCompraRepository, proveedorRepository);
        this.devolucionService = new DevolucionServiceImp(devolucionRepository, pedidoRepository);
        this.garantiaService = new GarantiaServiceImp(garantiaRepository, pedidoRepository);
        this.reporteService = new ReporteServiceImp(pedidoRepository);

        // 3. Datos de prueba: solo en modo memoria. Con MySQL los datos ya estan en la base
        //    (los inserta el script docs/database/magic_elixir_db.sql)
        if (!usarBaseDeDatos) {
            cargarDatosIniciales();
            cargarMovimientosDePrueba();
        }
    }

    //Metodos

    private void cargarDatosIniciales() {

        empleadoService.createEmpleado(1, "Admin", "Magic Elixir", "admin@magicelixir.com", TipoDocumento.CC,
                "3000000000", "Medellin", "2000-01-01", "Vendedor", "1234");
        empleadoService.createEmpleado(2, "Laura", "Zapata", "laura@magicelixir.com", TipoDocumento.CC,
                "3009998877", "Envigado", "1999-09-09", "Bodega", "1234");

        administradorService.createAdministrador(1, "Aaron Mateo", "Ruiz", "aaron@magicelixir.com", TipoDocumento.CC,
                "3001112233", "Medellin", "2000-05-10", "Total", "1");

        clienteService.createCliente(1, "Anyi", "Cañas", "anyi@gmail.com", TipoDocumento.CC, "3001234567",
                "Calle 10 #20-30", "2001-03-15", "Frecuente", "Calle 10 #20-30");
        clienteService.createCliente(2, "Valentina", "Gómez", "valegomez@gmail.com", TipoDocumento.CC, "3105558899",
                "Cra 43A #1-50, El Poblado", "1998-07-22", "Nuevo", "Cra 43A #1-50, El Poblado");
        clienteService.createCliente(3, "Camilo", "Restrepo", "camilo.res@hotmail.com", TipoDocumento.CC, "3017773344",
                "Calle 33 #65-12, Laureles", "1995-11-02", "Frecuente", "Calle 33 #65-12, Laureles");
        clienteService.createCliente(4, "Mariana", "Ospina", "mari.ospina@gmail.com", TipoDocumento.CE, "3226661122",
                "Av. El Poblado #10-20", "2000-02-14", "Mayorista", "Calle 50 #40-10, Itagüí");

        proveedorService.createProveedor(1, "Distribuidora Arabe SAS", "900123456", "6044445566",
                "ventas@distriarabe.com", "Bogota");
        proveedorService.createProveedor(2, "Perfumes Importados del Caribe", "901555777", "6053332211",
                "contacto@pidcaribe.com", "Barranquilla");

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

    // Unos pedidos, envios, ordenes y una devolucion para que las pantallas
    // no salgan vacias al presentar. Se hace todo con los services, igual que el usuario.
    private void cargarMovimientosDePrueba() {

        // Pedido 1: cliente de Instagram, ya pago por PSE y va en camino
        pedidoService.createPedido(1, 1, MetodoDePago.PSE, CanalVenta.INSTAGRAM, "Cliente de Instagram, empacar para regalo");
        pedidoService.agregarPerfume(1, 2, 1);
        pedidoService.agregarPerfume(1, 15, 1);
        pedidoService.cambiarEstadoPedido(1, EstadoPedido.PAGADO);
        envioService.createEnvio(1, 1, "Interrapidisimo", "IR-778812", "Calle 10 #20-30", 9500);
        envioService.actualizarEstadoEnvio(1, EstadoEnvio.EN_CAMINO);

        // Pedido 2: contra entrega, todavia pendiente
        pedidoService.createPedido(2, 2, MetodoDePago.EFECTIVO, CanalVenta.WHATSAPP, "Pago contra entrega por WhatsApp");
        pedidoService.agregarPerfume(2, 1, 2);

        // Pedido 3: venta en el local, enviada y entregada
        pedidoService.createPedido(3, 3, MetodoDePago.T_CREDITO, CanalVenta.LOCAL, "Venta en el local del C.C. Monterrey");
        pedidoService.agregarPerfume(3, 36, 1);
        pedidoService.cambiarEstadoPedido(3, EstadoPedido.PAGADO);
        envioService.createEnvio(2, 3, "Servientrega", "SV-100245", "Calle 33 #65-12, Laureles", 12000);
        envioService.actualizarEstadoEnvio(2, EstadoEnvio.ENTREGADO);

        // HU-16: ordenes de compra a los proveedores
        ordenCompraService.createOrdenCompra(1, 1, 4500000, "Reposicion de Lattafa y Armaf");
        ordenCompraService.cambiarEstadoOrdenCompra(1, EstadoOrdenCompra.APROBADA);
        ordenCompraService.createOrdenCompra(2, 2, 2800000, "Perfumeria femenina para diciembre");

        // HU-20: garantia de 6 meses para el pedido 3
        garantiaService.createGarantia(1, 3, java.time.LocalDate.now(), 6, "Garantia de fabrica contra defectos");

        // HU-17: devolucion del pedido 3 por arrepentimiento (se devuelve el 80%).
        // Queda SOLICITADA para que el administrador la apruebe o rechace (HU-18)
        Pedido pedidoEntregado = pedidoService.selectPedidoById(3);
        Envio envioEntregado = envioService.selectEnvioById(2);
        devolucionService.createDevolucion(1, 3, envioEntregado.getFechaEntrega(),
                MotivoDevolucion.ARREPENTIMIENTO_DE_COMPRA, "El cliente queria una fragancia mas dulce",
                pedidoEntregado.getTotalCostoPedido());

        // HU-07: algunos perfumes con poco stock para ver las alertas
        inventarioService.updateStock(5, 3);
        inventarioService.updateStock(12, 0);
        inventarioService.updateStock(40, 4);
    }

    //Getters

    public ClienteServiceInterface getClienteService() {
        return clienteService;
    }

    public EmpleadoServiceInterface getEmpleadoService() {
        return empleadoService;
    }

    public AdministradorServiceInterface getAdministradorService() {
        return administradorService;
    }

    public CategoriaServiceInterface getCategoriaService() {
        return categoriaService;
    }

    public PerfumeServiceInterface getPerfumeService() {
        return perfumeService;
    }

    public InventarioServiceInterface getInventarioService() {
        return inventarioService;
    }

    public PedidoServiceInterface getPedidoService() {
        return pedidoService;
    }

    public EnvioServiceInterface getEnvioService() {
        return envioService;
    }

    public ProveedorServiceInterface getProveedorService() {
        return proveedorService;
    }

    public OrdenCompraServiceInterface getOrdenCompraService() {
        return ordenCompraService;
    }

    public DevolucionServiceInterface getDevolucionService() {
        return devolucionService;
    }

    public GarantiaServiceInterface getGarantiaService() {
        return garantiaService;
    }

    public ReporteServiceInterface getReporteService() {
        return reporteService;
    }

    public boolean isUsandoBaseDeDatos() {
        return usandoBaseDeDatos;
    }

}//Fin public class
