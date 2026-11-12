package repository.mapper;

import domain.Cliente;
import domain.Pedido;
import domain.enums.CanalVenta;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PedidoRowMapper implements RowMapper<Pedido> {

    @Override
    public Pedido mapRow(ResultSet rs) throws SQLException {

        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));
        pedido.setFechaPedido(rs.getDate("fecha_pedido").toLocalDate());
        // Solo se guarda el id; el repository trae el objeto completo (ver hidratar)
        Cliente clienteRef = new Cliente();
        clienteRef.setIdUsuario(rs.getInt("id_cliente"));
        pedido.setCliente(clienteRef);
        pedido.setTotalCostoPedido(rs.getDouble("total"));
        pedido.setObservaciones(rs.getString("observaciones"));
        String estadoPedidoTexto = rs.getString("estado_pedido");
        pedido.setEstadoPedido(estadoPedidoTexto == null ? null : EstadoPedido.valueOf(estadoPedidoTexto));
        String metodoPagoTexto = rs.getString("metodo_pago");
        pedido.setMetodoPago(metodoPagoTexto == null ? null : MetodoDePago.valueOf(metodoPagoTexto));
        String canalVentaTexto = rs.getString("canal_venta");
        pedido.setCanalVenta(canalVentaTexto == null ? null : CanalVenta.valueOf(canalVentaTexto));

        return pedido;
    }

}
