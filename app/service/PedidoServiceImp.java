package service;

import domain.Cliente;
import domain.DetallePedido;
import domain.Inventario;
import domain.Pedido;
import domain.Perfume;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;
import service.helpers.SetInventarioHelper;
import service.inputport.PedidoServiceInterface;
import service.outputport.ClienteRepositoryPort;
import service.outputport.InventarioRepositoryPort;
import service.outputport.PedidoRepositoryPort;
import service.outputport.PerfumeRepositoryPort;

import java.time.LocalDate;
import java.util.List;

public class PedidoServiceImp implements PedidoServiceInterface {

    private final PedidoRepositoryPort pedidoRepository;
    private final ClienteRepositoryPort clienteRepository;
    private final PerfumeRepositoryPort perfumeRepository;
    private final InventarioRepositoryPort inventarioRepository;

    public PedidoServiceImp(PedidoRepositoryPort pedidoRepository, ClienteRepositoryPort clienteRepository,
                            PerfumeRepositoryPort perfumeRepository, InventarioRepositoryPort inventarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.perfumeRepository = perfumeRepository;
        this.inventarioRepository = inventarioRepository;
    }

    // HU-09 y HU-12: crear el pedido con el cliente y el metodo de pago
    @Override
    public Pedido createPedido(int idPedido, int idCliente, MetodoDePago metodoPago, String observaciones) {
        Cliente cliente = clienteRepository.selectClienteById(idCliente);
        if (cliente == null) {
            return null;
        }

        Pedido pedido = new Pedido(idPedido, LocalDate.now(), cliente, observaciones, EstadoPedido.PENDIENTE, metodoPago);
        return pedidoRepository.createPedido(pedido);
    }

    // HU-10, HU-11 y HU-08: agregar perfumes, recalcular el total y validar el stock
    @Override
    public String agregarPerfume(int idPedido, int idPerfume, int cantidad) {
        Pedido pedido = pedidoRepository.selectPedidoById(idPedido);
        if (pedido == null) {
            return "No existe el pedido";
        }
        if (pedido.getEstadoPedido() != EstadoPedido.PENDIENTE) {
            return "Solo se pueden agregar perfumes a pedidos pendientes";
        }

        Perfume perfume = perfumeRepository.selectPerfumeById(idPerfume);
        if (perfume == null) {
            return "No existe el perfume";
        }
        if (cantidad <= 0) {
            return "La cantidad debe ser mayor a 0";
        }

        Inventario inventario = inventarioRepository.selectInventarioByPerfume(idPerfume);
        if (inventario == null || inventario.getStockDisponible() < cantidad) {
            return "No hay stock suficiente de " + perfume.getNombre();
        }

        // Se descuenta del inventario
        int nuevoStock = inventario.getStockDisponible() - cantidad;
        inventario.setStockDisponible(nuevoStock);
        inventario.setEstadoDisponibilidad(SetInventarioHelper.calcularEstado(nuevoStock));
        inventarioRepository.updateInventario(inventario);

        // Se agrega el detalle
        int idDetalle = pedido.getDetalles().size() + 1;
        double subtotal = perfume.getPrecio() * cantidad;
        DetallePedido detalle = new DetallePedido(idDetalle, perfume, cantidad, perfume.getPrecio(), subtotal);
        pedido.getDetalles().add(detalle);

        // Se recalcula el total
        double total = 0;
        for (DetallePedido d : pedido.getDetalles()) {
            total = total + d.getSubtotal();
        }
        pedido.setTotalCostoPedido(total);

        pedidoRepository.updatePedido(pedido);
        return "Perfume agregado. Total del pedido: $" + total;
    }

    @Override
    public Pedido selectPedidoById(int id) {
        return pedidoRepository.selectPedidoById(id);
    }

    @Override
    public List<Pedido> selectAllPedidos() {
        return pedidoRepository.selectAllPedidos();
    }

    @Override
    public Pedido updatePedido(int idPedido, MetodoDePago metodoPago, String observaciones) {
        Pedido pedido = pedidoRepository.selectPedidoById(idPedido);
        if (pedido == null) {
            return null;
        }

        pedido.setMetodoPago(metodoPago);
        pedido.setObservaciones(observaciones);

        return pedidoRepository.updatePedido(pedido);
    }

    @Override
    public Pedido cambiarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.selectPedidoById(idPedido);
        if (pedido == null) {
            return null;
        }

        // Si se cancela, los perfumes vuelven al inventario
        if (nuevoEstado == EstadoPedido.CANCELADO && pedido.getEstadoPedido() != EstadoPedido.CANCELADO) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                Inventario inventario = inventarioRepository.selectInventarioByPerfume(detalle.getPerfume().getIdPerfume());
                if (inventario != null) {
                    int nuevoStock = inventario.getStockDisponible() + detalle.getCantidad();
                    inventario.setStockDisponible(nuevoStock);
                    inventario.setEstadoDisponibilidad(SetInventarioHelper.calcularEstado(nuevoStock));
                    inventarioRepository.updateInventario(inventario);
                }
            }
        }

        pedido.setEstadoPedido(nuevoEstado);
        return pedidoRepository.updatePedido(pedido);
    }

    @Override
    public void deletePedidoById(int id) {
        pedidoRepository.deletePedidoById(id);
    }

}
