package api;

import com.sun.net.httpserver.HttpExchange;
import domain.enums.MetodoDePago;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// GET /api/metodos-pago -> las opciones del enum MetodoDePago para el carrito
public class MetodoPagoController extends BaseController {

    @Override
    protected void atender(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            throw metodoNoPermitido();
        }
        List<String> metodos = new ArrayList<>();
        for (MetodoDePago metodo : MetodoDePago.values()) {
            metodos.add(new Json.Objeto()
                    .campo("codigo", metodo.name())
                    .campo("descripcion", metodo.getDescripcion())
                    .toString());
        }
        responder(exchange, 200, Json.lista(metodos));
    }

}
