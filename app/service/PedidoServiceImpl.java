package service;

import domain.*;
import repository.IPedidoRepository;
import service.helpers.PedidoUtil;

public class PedidoServiceImpl implements IPedidoService{

    // Inyección del Repositorio (DIP - Inversión de Dependencias)
    private final IPedidoRepository repository;

    public PedidoServiceImpl(IPedidoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pedido crearPedido(int idPedido, Cliente cliente, MetodoDePago metodoPago, String observaciones) {
        return new Pedido(idPedido, cliente, metodoPago, observaciones);
    }

    @Override
    public void agregarDetalle(Pedido pedido, Perfume perfume, int cantidad) {
        if (cantidad <= 0) {
            System.out.println("❌ Error: La cantidad debe ser mayor a 0.");
            return;
        }

        int idDetalle = pedido.getDetalles().size() + 1;
        DetallePedido detalle = new DetallePedido(idDetalle, perfume, cantidad);
        pedido.getDetalles().add(detalle);

        // Actualiza el total global del pedido automáticamente
        calcularTotal(pedido);
    }

    @Override
    public void calcularTotal(Pedido pedido) {
        double total = 0.0;
        for (DetallePedido detalle : pedido.getDetalles()) {
            total += detalle.getSubtotal();
        }
        pedido.setTotalCostoPedido(total);
    }

    @Override
    public boolean confirmarPedido(Pedido pedido) {
        if (!PedidoUtil.esPedidoValido(pedido)) {
            System.out.println("❌ Error: No se puede confirmar un pedido sin productos.");
            return false;
        }

        // Cambia el estado y lo guarda en el repositorio
        pedido.setEstadoPedido(EstadoPedido.PAGADO);
        repository.guardar(pedido);

        System.out.println("✅ Pedido #" + pedido.getIdPedido() + " confirmado.");
        System.out.println("   Monto Total: " + PedidoUtil.formatearMoneda(pedido.getTotalCostoPedido()));
        return true;
    }

    @Override
    public void cancelarPedido(Pedido pedido) {
        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        System.out.println("⚠️ El pedido #" + pedido.getIdPedido() + " ha sido cancelado.");
    }

}
