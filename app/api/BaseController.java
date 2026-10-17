package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Clase abstracta de todos los controllers de la API: cada uno solo implementa atender(),
// y aqui se resuelve lo comun (leer el cuerpo, responder JSON, CORS y errores)
public abstract class BaseController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            agregarCors(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            atender(exchange);
        } catch (ErrorApi e) {
            responder(exchange, e.getEstado(), error(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            responder(exchange, 500, error("Error interno del servidor"));
        } finally {
            exchange.close();
        }
    }

    // Cada controller decide que hacer segun el metodo (GET, POST, PUT) y la ruta
    protected abstract void atender(HttpExchange exchange) throws IOException;

    protected void responder(HttpExchange exchange, int estado, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(estado, bytes.length);
        try (OutputStream salida = exchange.getResponseBody()) {
            salida.write(bytes);
        }
    }

    // Lo que viene despues de la ruta del controller: /api/clientes/5/pedidos -> "5/pedidos"
    protected String subruta(HttpExchange exchange) {
        String ruta = exchange.getRequestURI().getPath().substring(exchange.getHttpContext().getPath().length());
        while (ruta.startsWith("/")) {
            ruta = ruta.substring(1);
        }
        while (ruta.endsWith("/")) {
            ruta = ruta.substring(0, ruta.length() - 1);
        }
        return ruta;
    }

    // ?correo=anyi@gmail.com -> {correo=anyi@gmail.com}
    protected Map<String, String> parametros(HttpExchange exchange) {
        Map<String, String> parametros = new HashMap<>();
        String consulta = exchange.getRequestURI().getRawQuery();
        if (consulta == null) {
            return parametros;
        }
        for (String par : consulta.split("&")) {
            String[] partes = par.split("=", 2);
            String valor = partes.length > 1 ? partes[1] : "";
            parametros.put(URLDecoder.decode(partes[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(valor, StandardCharsets.UTF_8));
        }
        return parametros;
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> leerCuerpo(HttpExchange exchange) throws IOException {
        try (InputStream entrada = exchange.getRequestBody()) {
            String cuerpo = new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
            Object json = Json.leer(cuerpo);
            if (!(json instanceof Map)) {
                throw new ErrorApi(400, "Se esperaba un objeto JSON");
            }
            return (Map<String, Object>) json;
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new ErrorApi(400, "El cuerpo no es un JSON valido");
        }
    }

    // ---- Validaciones de los datos que llegan de la web ----

    protected static String texto(Map<String, Object> cuerpo, String campo) {
        Object valor = cuerpo.get(campo);
        if (!(valor instanceof String) || ((String) valor).trim().isEmpty()) {
            throw new ErrorApi(400, "El campo " + campo + " es obligatorio");
        }
        return ((String) valor).trim();
    }

    protected static int entero(Object valor, String campo) {
        if (!(valor instanceof Double) || (Double) valor != Math.floor((Double) valor)) {
            throw new ErrorApi(400, "El campo " + campo + " debe ser un numero entero");
        }
        return ((Double) valor).intValue();
    }

    protected static int entero(String valor, String campo) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new ErrorApi(400, "El " + campo + " debe ser un numero entero");
        }
    }

    @SuppressWarnings("unchecked")
    protected static List<Object> lista(Map<String, Object> cuerpo, String campo) {
        Object valor = cuerpo.get(campo);
        if (!(valor instanceof List) || ((List<Object>) valor).isEmpty()) {
            throw new ErrorApi(400, "El campo " + campo + " debe ser una lista con al menos un elemento");
        }
        return (List<Object>) valor;
    }

    protected static ErrorApi metodoNoPermitido() {
        return new ErrorApi(405, "Metodo no permitido");
    }

    protected static ErrorApi noEncontrado(String mensaje) {
        return new ErrorApi(404, mensaje);
    }

    private static String error(String mensaje) {
        return new Json.Objeto().campo("error", mensaje).toString();
    }

    // Permite que la pagina se abra tambien desde otro puerto (por ejemplo Live Server de VS Code)
    private void agregarCors(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

}
