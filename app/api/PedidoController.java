package api;

import com.sun.net.httpserver.HttpExchange;
import domain.Cliente;
import domain.Inventario;
import domain.Pedido;
import domain.Perfume;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;
import service.inputport.ClienteServiceInterface;
import service.inputport.InventarioServiceInterface;
import service.inputport.PedidoServiceInterface;
import service.inputport.PerfumeServiceInterface;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// POST /api/pedidos       -> el carrito de la web se convierte en un Pedido real (HU-09 a HU-12)
// GET  /api/pedidos/{id}  -> consultar un pedido
public class PedidoController extends BaseController {

    private final PedidoServiceInterface pedidoService;
    private final ClienteServiceInterface clienteService;
    private final PerfumeServiceInterface perfumeService;
    private final InventarioServiceInterface inventarioService;

    public PedidoController(PedidoServiceInterface pedidoService, ClienteServiceInterface clienteService,
                            PerfumeServiceInterface perfumeService, InventarioServiceInterface inventarioService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.perfumeService = perfumeService;
        this.inventarioService = inventarioService;
    }

    @Override
    protected void atender(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String ruta = subruta(exchange);

        if (ruta.isEmpty() && "POST".equals(metodo)) {
            crear(exchange);
        } else if (!ruta.isEmpty() && "GET".equals(metodo)) {
            Pedido pedido = pedidoService.selectPedidoById(entero(ruta, "id del pedido"));
            if (pedido == null) {
                throw noEncontrado("No existe el pedido");
            }
            responder(exchange, 200, JsonMapper.pedido(pedido));
        } else {
            throw metodoNoPermitido();
        }
    }

    @SuppressWarnings("unchecked")
    private void crear(HttpExchange exchange) throws IOException {
        Map<String, Object> cuerpo = leerCuerpo(exchange);

        int idCliente = entero(cuerpo.get("idCliente"), "idCliente");
        Cliente cliente = clienteService.selectClienteById(idCliente);
        if (cliente == null) {
            throw noEncontrado("No existe el cliente");
        }

        MetodoDePago metodoPago;
        try {
            metodoPago = MetodoDePago.valueOf(texto(cuerpo, "metodoPago"));
        } catch (IllegalArgumentException e) {
            throw new ErrorApi(400, "Metodo de pago no valido");
        }

        // Se juntan las cantidades por perfume: {idPerfume -> cantidad}
        Map<Integer, Integer> cantidades = new LinkedHashMap<>();
        for (Object item : lista(cuerpo, "items")) {
            if (!(item instanceof Map)) {
                throw new ErrorApi(400, "Cada item debe tener idPerfume y cantidad");
            }
            Map<String, Object> datos = (Map<String, Object>) item;
            int idPerfume = entero(datos.get("idPerfume"), "idPerfume");
            int cantidad = entero(datos.get("cantidad"), "cantidad");
            if (cantidad <= 0) {
                throw new ErrorApi(400, "La cantidad debe ser mayor a 0");
            }
            cantidades.merge(idPerfume, cantidad, Integer::sum);
        }

        // Primero se revisa todo el stock, para no dejar un pedido a medias
        for (Map.Entry<Integer, Integer> entrada : cantidades.entrySet()) {
            Perfume perfume = perfumeService.selectPerfumeById(entrada.getKey());
            if (perfume == null) {
                throw noEncontrado("No existe el perfume con id " + entrada.getKey());
            }
            Inventario inventario = inventarioService.selectInventarioByPerfume(perfume.getIdPerfume());
            int disponible = inventario == null ? 0 : inventario.getStockDisponible();
            if (disponible < entrada.getValue()) {
                throw new ErrorApi(409, "No hay stock suficiente de " + perfume.getNombre()
                        + " (quedan " + disponible + ")");
            }
        }

        // Se usa el mismo PedidoService de la consola: crea el pedido, agrega los perfumes,
        // descuenta el inventario y calcula el total
        Pedido pedido = pedidoService.createPedido(siguienteId(), idCliente, metodoPago, "Pedido desde la tienda web");
        for (Map.Entry<Integer, Integer> entrada : cantidades.entrySet()) {
            int detallesAntes = pedido.getDetalles().size();
            String resultado = pedidoService.agregarPerfume(pedido.getIdPedido(), entrada.getKey(), entrada.getValue());
            if (pedido.getDetalles().size() == detallesAntes) {
                // Si algo falla se cancela, y al cancelar el service devuelve el stock
                pedidoService.cambiarEstadoPedido(pedido.getIdPedido(), EstadoPedido.CANCELADO);
                throw new ErrorApi(409, resultado);
            }
        }

        responder(exchange, 201, JsonMapper.pedido(pedidoService.selectPedidoById(pedido.getIdPedido())));
    }

    private int siguienteId() {
        int mayor = 0;
        List<Pedido> pedidos = pedidoService.selectAllPedidos();
        for (Pedido pedido : pedidos) {
            mayor = Math.max(mayor, pedido.getIdPedido());
        }
        return mayor + 1;
    }

}
