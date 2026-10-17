package api;

import domain.Categoria;
import domain.Cliente;
import domain.DetallePedido;
import domain.Inventario;
import domain.Pedido;
import domain.Perfume;

import java.util.ArrayList;
import java.util.List;

// Convierte los POJO del dominio en el JSON que recibe la pagina web
public final class JsonMapper {

    private JsonMapper() {
    }

    public static String categoria(Categoria categoria) {
        return new Json.Objeto()
                .campo("id", categoria.getIdCategoria())
                .campo("nombre", categoria.getNombre())
                .campo("descripcion", categoria.getDescripcion())
                .toString();
    }

    // El stock viene del inventario (HU-07), no del perfume
    public static String perfume(Perfume perfume, Inventario inventario) {
        Categoria categoria = perfume.getCategoria();
        return new Json.Objeto()
                .campo("id", perfume.getIdPerfume())
                .campo("nombre", perfume.getNombre())
                .campo("marca", perfume.getMarca())
                .campo("precio", perfume.getPrecio())
                .campo("mililitros", perfume.getMililitros())
                .campo("notasOlfativas", perfume.getNotasOlfativas())
                .crudo("categoria", categoria == null ? "null" : categoria(categoria))
                .campo("stock", inventario == null ? 0 : inventario.getStockDisponible())
                .campo("estadoInventario", inventario == null ? "AGOTADO" : inventario.getEstadoDisponibilidad().name())
                .toString();
    }

    public static String cliente(Cliente cliente) {
        return new Json.Objeto()
                .campo("id", cliente.getIdUsuario())
                .campo("nombre", cliente.getNombre())
                .campo("apellido", cliente.getApellido())
                .campo("correo", cliente.getCorreo())
                .campo("celular", cliente.getCelular())
                .campo("direccion", cliente.getDireccion())
                .campo("direccionEnvio", cliente.getDireccionEnvio())
                .campo("tipoCliente", cliente.getTipoCliente())
                .toString();
    }

    public static String pedido(Pedido pedido) {
        List<String> detalles = new ArrayList<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalles.add(detalle(detalle));
        }
        return new Json.Objeto()
                .campo("id", pedido.getIdPedido())
                .campo("fecha", String.valueOf(pedido.getFechaPedido()))
                .campo("idCliente", pedido.getCliente().getIdUsuario())
                .campo("cliente", pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido())
                .campo("direccionEnvio", pedido.getCliente().getDireccionEnvio())
                .campo("estado", pedido.getEstadoPedido().name())
                .campo("estadoDescripcion", pedido.getEstadoPedido().getDescripcion())
                .campo("metodoPago", pedido.getMetodoPago().name())
                .campo("metodoPagoDescripcion", pedido.getMetodoPago().getDescripcion())
                .campo("observaciones", pedido.getObservaciones())
                .crudo("detalles", Json.lista(detalles))
                .campo("total", pedido.getTotalCostoPedido())
                .toString();
    }

    private static String detalle(DetallePedido detalle) {
        return new Json.Objeto()
                .campo("idPerfume", detalle.getPerfume().getIdPerfume())
                .campo("nombre", detalle.getPerfume().getNombre())
                .campo("marca", detalle.getPerfume().getMarca())
                .campo("cantidad", detalle.getCantidad())
                .campo("precioUnitario", detalle.getPrecioUnitario())
                .campo("subtotal", detalle.getSubtotal())
                .toString();
    }

}
