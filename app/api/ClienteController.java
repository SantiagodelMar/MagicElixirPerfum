package api;

import com.sun.net.httpserver.HttpExchange;
import domain.Cliente;
import domain.Pedido;
import domain.enums.TipoDocumento;
import service.inputport.ClienteServiceInterface;
import service.inputport.PedidoServiceInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// GET  /api/clientes?correo=x      -> ingresar a la tienda con el correo del cliente
// POST /api/clientes               -> registrarse desde la web
// PUT  /api/clientes/{id}          -> actualizar celular y direccion de envio
// GET  /api/clientes/{id}/pedidos  -> pedidos del cliente con su estado
public class ClienteController extends BaseController {

    private final ClienteServiceInterface clienteService;
    private final PedidoServiceInterface pedidoService;

    public ClienteController(ClienteServiceInterface clienteService, PedidoServiceInterface pedidoService) {
        this.clienteService = clienteService;
        this.pedidoService = pedidoService;
    }

    @Override
    protected void atender(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String[] partes = subruta(exchange).split("/");

        if (partes[0].isEmpty()) {
            if ("GET".equals(metodo)) {
                buscarPorCorreo(exchange);
            } else if ("POST".equals(metodo)) {
                registrar(exchange);
            } else {
                throw metodoNoPermitido();
            }
            return;
        }

        Cliente cliente = clienteService.selectClienteById(entero(partes[0], "id del cliente"));
        if (cliente == null) {
            throw noEncontrado("No existe el cliente");
        }

        if (partes.length == 1 && "PUT".equals(metodo)) {
            actualizar(exchange, cliente);
        } else if (partes.length == 1 && "GET".equals(metodo)) {
            responder(exchange, 200, JsonMapper.cliente(cliente));
        } else if (partes.length == 2 && "pedidos".equals(partes[1]) && "GET".equals(metodo)) {
            pedidosDelCliente(exchange, cliente);
        } else {
            throw metodoNoPermitido();
        }
    }

    private void buscarPorCorreo(HttpExchange exchange) throws IOException {
        String correo = parametros(exchange).get("correo");
        if (correo == null || correo.trim().isEmpty()) {
            throw new ErrorApi(400, "Falta el correo");
        }
        Cliente cliente = clientePorCorreo(correo.trim());
        if (cliente == null) {
            throw noEncontrado("No hay ningun cliente registrado con ese correo");
        }
        responder(exchange, 200, JsonMapper.cliente(cliente));
    }

    private void registrar(HttpExchange exchange) throws IOException {
        Map<String, Object> cuerpo = leerCuerpo(exchange);
        String nombre = texto(cuerpo, "nombre");
        String apellido = texto(cuerpo, "apellido");
        String correo = texto(cuerpo, "correo");
        String celular = texto(cuerpo, "celular");
        String direccionEnvio = texto(cuerpo, "direccionEnvio");

        if (!correo.contains("@") || !correo.contains(".")) {
            throw new ErrorApi(400, "El correo no es valido");
        }
        if (clientePorCorreo(correo) != null) {
            throw new ErrorApi(409, "Ya existe un cliente con ese correo");
        }

        // Los clientes que llegan por la web quedan marcados como tipo "Web"
        Cliente cliente = clienteService.createCliente(siguienteId(), nombre, apellido, correo, TipoDocumento.CC,
                celular, direccionEnvio, "", "Web", direccionEnvio);
        responder(exchange, 201, JsonMapper.cliente(cliente));
    }

    private void actualizar(HttpExchange exchange, Cliente cliente) throws IOException {
        Map<String, Object> cuerpo = leerCuerpo(exchange);
        String celular = texto(cuerpo, "celular");
        String direccionEnvio = texto(cuerpo, "direccionEnvio");
        Cliente actualizado = clienteService.updateCliente(cliente.getIdUsuario(), celular,
                cliente.getDireccion(), direccionEnvio);
        responder(exchange, 200, JsonMapper.cliente(actualizado));
    }

    private void pedidosDelCliente(HttpExchange exchange, Cliente cliente) throws IOException {
        List<String> pedidos = new ArrayList<>();
        for (Pedido pedido : pedidoService.selectAllPedidos()) {
            if (pedido.getCliente().getIdUsuario() == cliente.getIdUsuario()) {
                pedidos.add(JsonMapper.pedido(pedido));
            }
        }
        responder(exchange, 200, Json.lista(pedidos));
    }

    private Cliente clientePorCorreo(String correo) {
        for (Cliente cliente : clienteService.selectAllClientes()) {
            if (cliente.getCorreo() != null && cliente.getCorreo().equalsIgnoreCase(correo)) {
                return cliente;
            }
        }
        return null;
    }

    private int siguienteId() {
        int mayor = 0;
        for (Cliente cliente : clienteService.selectAllClientes()) {
            mayor = Math.max(mayor, cliente.getIdUsuario());
        }
        return mayor + 1;
    }

}
