package api;

import com.sun.net.httpserver.HttpExchange;
import domain.Perfume;
import service.inputport.InventarioServiceInterface;
import service.inputport.PerfumeServiceInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// GET /api/perfumes       -> catalogo completo con el stock del inventario
// GET /api/perfumes/{id}  -> un perfume
public class PerfumeController extends BaseController {

    private final PerfumeServiceInterface perfumeService;
    private final InventarioServiceInterface inventarioService;

    public PerfumeController(PerfumeServiceInterface perfumeService, InventarioServiceInterface inventarioService) {
        this.perfumeService = perfumeService;
        this.inventarioService = inventarioService;
    }

    @Override
    protected void atender(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            throw metodoNoPermitido();
        }

        String ruta = subruta(exchange);
        if (ruta.isEmpty()) {
            List<String> perfumes = new ArrayList<>();
            for (Perfume perfume : perfumeService.selectAllPerfumes()) {
                perfumes.add(aJson(perfume));
            }
            responder(exchange, 200, Json.lista(perfumes));
            return;
        }

        Perfume perfume = perfumeService.selectPerfumeById(entero(ruta, "id del perfume"));
        if (perfume == null) {
            throw noEncontrado("No existe el perfume");
        }
        responder(exchange, 200, aJson(perfume));
    }

    private String aJson(Perfume perfume) {
        return JsonMapper.perfume(perfume, inventarioService.selectInventarioByPerfume(perfume.getIdPerfume()));
    }

}
