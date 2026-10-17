package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

// Entrega los archivos de la carpeta web/ (index.html, css, js, imagenes)
public class ArchivosWebHandler implements HttpHandler {

    private static final Map<String, String> TIPOS = new HashMap<>();

    static {
        TIPOS.put("html", "text/html; charset=utf-8");
        TIPOS.put("css", "text/css; charset=utf-8");
        TIPOS.put("js", "text/javascript; charset=utf-8");
        TIPOS.put("png", "image/png");
        TIPOS.put("svg", "image/svg+xml");
        TIPOS.put("ttf", "font/ttf");
    }

    private final File carpeta;

    public ArchivosWebHandler(File carpeta) {
        this.carpeta = carpeta;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String ruta = exchange.getRequestURI().getPath();
            File archivo = new File(carpeta, ruta.equals("/") ? "index.html" : ruta.substring(1));

            // Solo se entregan archivos que esten dentro de web/
            boolean dentroDeWeb = archivo.getCanonicalPath().startsWith(carpeta.getCanonicalPath() + File.separator);
            if (!"GET".equals(exchange.getRequestMethod()) || !dentroDeWeb || !archivo.isFile()) {
                byte[] mensaje = "No encontrado".getBytes();
                exchange.sendResponseHeaders(404, mensaje.length);
                try (OutputStream salida = exchange.getResponseBody()) {
                    salida.write(mensaje);
                }
                return;
            }

            String nombre = archivo.getName();
            String extension = nombre.substring(nombre.lastIndexOf('.') + 1).toLowerCase();
            byte[] contenido = Files.readAllBytes(archivo.toPath());
            exchange.getResponseHeaders().set("Content-Type", TIPOS.getOrDefault(extension, "application/octet-stream"));
            exchange.sendResponseHeaders(200, contenido.length);
            try (OutputStream salida = exchange.getResponseBody()) {
                salida.write(contenido);
            }
        } finally {
            exchange.close();
        }
    }

}
