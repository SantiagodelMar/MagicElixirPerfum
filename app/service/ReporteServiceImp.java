package service;

import domain.DetallePedido;
import domain.ItemReporte;
import domain.Pedido;
import domain.enums.EstadoPedido;
import service.inputport.ReporteServiceInterface;
import service.outputport.PedidoRepositoryPort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Reportes del administrador (Epica 8). Se calculan con los pedidos que entrega el repository,
// asi funcionan igual con la lista en memoria que con MySQL.
public class ReporteServiceImp implements ReporteServiceInterface {

    private final PedidoRepositoryPort pedidoRepository;

    public ReporteServiceImp(PedidoRepositoryPort pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // HU-22: total vendido entre dos fechas (incluye las dos)
    @Override
    public double totalVendido(LocalDate desde, LocalDate hasta) {
        double total = 0;
        for (Pedido pedido : pedidosVendidos(desde, hasta)) {
            total = total + pedido.getTotalCostoPedido();
        }
        return total;
    }

    // HU-23: perfumes con mas unidades vendidas (de mayor a menor)
    @Override
    public List<ItemReporte> perfumesMasVendidos(LocalDate desde, LocalDate hasta, int limite) {
        Map<String, ItemReporte> acumulado = new LinkedHashMap<>();
        for (Pedido pedido : pedidosVendidos(desde, hasta)) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                String nombre = detalle.getPerfume().getNombre() + " (" + detalle.getPerfume().getMarca() + ")";
                ItemReporte item = acumulado.get(nombre);
                if (item == null) {
                    item = new ItemReporte(nombre, 0, 0);
                    acumulado.put(nombre, item);
                }
                item.setCantidad(item.getCantidad() + detalle.getCantidad());
                item.setTotal(item.getTotal() + detalle.getSubtotal());
            }
        }
        List<ItemReporte> lista = new ArrayList<>(acumulado.values());
        lista.sort((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()));
        if (limite > 0 && lista.size() > limite) {
            return new ArrayList<>(lista.subList(0, limite));
        }
        return lista;
    }

    // HU-24: cuantos pedidos y cuanto dinero trajo cada canal
    @Override
    public List<ItemReporte> ventasPorCanal(LocalDate desde, LocalDate hasta) {
        Map<String, ItemReporte> acumulado = new LinkedHashMap<>();
        for (Pedido pedido : pedidosVendidos(desde, hasta)) {
            String canal = pedido.getCanalVenta() == null ? "Sin canal" : pedido.getCanalVenta().getDescripcion();
            ItemReporte item = acumulado.get(canal);
            if (item == null) {
                item = new ItemReporte(canal, 0, 0);
                acumulado.put(canal, item);
            }
            item.setCantidad(item.getCantidad() + 1);
            item.setTotal(item.getTotal() + pedido.getTotalCostoPedido());
        }
        List<ItemReporte> lista = new ArrayList<>(acumulado.values());
        lista.sort((a, b) -> Double.compare(b.getTotal(), a.getTotal()));
        return lista;
    }

    // Una venta cuenta cuando el pedido ya se pago (pagado, enviado o entregado) y esta en el rango
    private List<Pedido> pedidosVendidos(LocalDate desde, LocalDate hasta) {
        List<Pedido> vendidos = new ArrayList<>();
        for (Pedido pedido : pedidoRepository.selectAllPedidos()) {
            EstadoPedido estado = pedido.getEstadoPedido();
            boolean pagado = estado == EstadoPedido.PAGADO || estado == EstadoPedido.ENVIADO
                    || estado == EstadoPedido.ENTREGADO;
            boolean enRango = !pedido.getFechaPedido().isBefore(desde) && !pedido.getFechaPedido().isAfter(hasta);
            if (pagado && enRango) {
                vendidos.add(pedido);
            }
        }
        return vendidos;
    }

}
