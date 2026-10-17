package api;

import com.sun.net.httpserver.HttpExchange;
import domain.Categoria;
import service.inputport.CategoriaServiceInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// GET /api/categorias -> las 4 categorias (HU-06), para los filtros de la tienda
public class CategoriaController extends BaseController {

    private final CategoriaServiceInterface categoriaService;

    public CategoriaController(CategoriaServiceInterface categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    protected void atender(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            throw metodoNoPermitido();
        }
        List<String> categorias = new ArrayList<>();
        for (Categoria categoria : categoriaService.selectAllCategorias()) {
            categorias.add(JsonMapper.categoria(categoria));
        }
        responder(exchange, 200, Json.lista(categorias));
    }

}
