package api;

import com.sun.net.httpserver.HttpServer;
import config.Contexto;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

// Servidor HTTP que trae Java (no hay que instalar nada). Expone los services como API JSON
// en /api/... y entrega la tienda web de la carpeta web/ en http://localhost:8080
public class ApiServer {

    public static final int PUERTO = 8080;

    private final HttpServer servidor;

    public ApiServer(Contexto contexto, int puerto) throws IOException {
        // Solo escucha en este computador (localhost)
        servidor = HttpServer.create(new InetSocketAddress("localhost", puerto), 0);

        servidor.createContext("/api/categorias", new CategoriaController(contexto.getCategoriaService()));
        servidor.createContext("/api/perfumes", new PerfumeController(contexto.getPerfumeService(),
                contexto.getInventarioService()));
        servidor.createContext("/api/clientes", new ClienteController(contexto.getClienteService(),
                contexto.getPedidoService()));
        servidor.createContext("/api/pedidos", new PedidoController(contexto.getPedidoService(),
                contexto.getClienteService(), contexto.getPerfumeService(), contexto.getInventarioService()));
        servidor.createContext("/api/metodos-pago", new MetodoPagoController());
        servidor.createContext("/", new ArchivosWebHandler(carpetaWeb()));
    }

    public void iniciar() {
        servidor.start();
        System.out.println("Tienda web disponible en http://localhost:" + servidor.getAddress().getPort());
    }

    public void detener() {
        servidor.stop(0);
    }

    // IntelliJ ejecuta desde la raiz del proyecto, pero por si acaso se busca tambien un nivel arriba
    private static File carpetaWeb() {
        File web = new File("web");
        if (!web.isDirectory()) {
            web = new File("../web");
        }
        if (!web.isDirectory()) {
            System.out.println("Aviso: no se encontro la carpeta web/, solo funcionara la API");
        }
        return web.getAbsoluteFile();
    }

}
