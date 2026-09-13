package service;

import domain.Cliente;
import domain.MetodoDePago;
import domain.Pedido;
import domain.Perfume;

public interface IPedidoService {

    Pedido crearPedido(int idPedido, Cliente cliente, MetodoDePago metodoPago, String observaciones);
    void agregarDetalle(Pedido pedido, Perfume perfume, int cantidad);
    void calcularTotal(Pedido pedido);
    boolean confirmarPedido(Pedido pedido);
    void cancelarPedido(Pedido pedido);

}
