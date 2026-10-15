package repository;

import domain.Pedido;
import service.outputport.PedidoRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class PedidoRepositoryImp implements PedidoRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Pedido> pedidos = new ArrayList<>();

    @Override
    public Pedido createPedido(Pedido pedido) {
        pedidos.add(pedido);
        return pedido;
    }

    @Override
    public Pedido selectPedidoById(int id) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getIdPedido() == id) {
                return pedidos.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Pedido> selectAllPedidos() {
        return pedidos;
    }

    @Override
    public Pedido updatePedido(Pedido pedido) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getIdPedido() == pedido.getIdPedido()) {
                pedidos.set(i, pedido);
                return pedido;
            }
        }
        return null;
    }

    @Override
    public void deletePedidoById(int id) {
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getIdPedido() == id) {
                pedidos.remove(i);
                return;
            }
        }
    }

}
