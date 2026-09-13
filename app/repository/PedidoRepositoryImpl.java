package repository;

import domain.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoRepositoryImpl implements IPedidoRepository {

    // Simulación de la Base de Datos en memoria
    private final List <Pedido> baseDeDatos = new ArrayList<>();

    @Override
    public void guardar(Pedido pedido) {
        baseDeDatos.add(pedido);
        System.out.println("[BD] Pedido #" + pedido.getIdPedido() + " guardado con éxito.");
    }

    @Override
    public Pedido buscarPorId(int id) {
        for (Pedido p : baseDeDatos) {
            if (p.getIdPedido() == id) return p;
        }
        return null;// Si no encuentra el pedido
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return baseDeDatos;
    }


}
