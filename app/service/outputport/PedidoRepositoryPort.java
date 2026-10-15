package service.outputport;

import domain.Pedido;

import java.util.List;

public interface PedidoRepositoryPort {

    public Pedido createPedido(Pedido pedido);
    public Pedido selectPedidoById(int id);
    public List<Pedido> selectAllPedidos();
    public Pedido updatePedido(Pedido pedido);
    public void deletePedidoById(int id);

}
