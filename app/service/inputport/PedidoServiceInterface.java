package service.inputport;

import domain.Pedido;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;

import java.util.List;

public interface PedidoServiceInterface {

    public Pedido createPedido(int idPedido, int idCliente, MetodoDePago metodoPago, String observaciones);
    public String agregarPerfume(int idPedido, int idPerfume, int cantidad);
    public Pedido selectPedidoById(int id);
    public List<Pedido> selectAllPedidos();
    public Pedido updatePedido(int idPedido, MetodoDePago metodoPago, String observaciones);
    public Pedido cambiarEstadoPedido(int idPedido, EstadoPedido nuevoEstado);
    public void deletePedidoById(int id);

}
