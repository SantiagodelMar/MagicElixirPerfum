package config;

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

import java.util.List;

// Arma repositories y services una sola vez para que la consola (ui) y la web (api)
// trabajen sobre los mismos datos en memoria
public class Contexto {

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

    public Contexto() {
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
        this.devolucionService = new DevolucionServiceImp(devolucionRepository);

        cargarDatosIniciales();
    }

    // Datos de prueba para no empezar con todo vacio
    private void cargarDatosIniciales() {

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

    // Getters

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

}
