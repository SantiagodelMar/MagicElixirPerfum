package repository;

import domain.Pedido;

import java.util.List;

public interface IPedidoRepository {

    void guardar (Pedido pedido);
    Pedido buscarPorId(int id);
    List <Pedido> obtenerTodos();

}
