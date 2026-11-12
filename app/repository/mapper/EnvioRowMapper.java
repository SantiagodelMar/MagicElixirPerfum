package repository.mapper;

import domain.Envio;
import domain.Pedido;
import domain.enums.EstadoEnvio;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnvioRowMapper implements RowMapper<Envio> {

    @Override
    public Envio mapRow(ResultSet rs) throws SQLException {

        Envio envio = new Envio();
        envio.setIdEnvio(rs.getInt("id_envio"));
        // Solo se guarda el id; el repository trae el objeto completo (ver hidratar)
        Pedido pedidoRef = new Pedido();
        pedidoRef.setIdPedido(rs.getInt("id_pedido"));
        envio.setPedido(pedidoRef);
        envio.setTransportadora(rs.getString("transportadora"));
        envio.setNumeroDeGuia(rs.getString("numero_guia"));
        envio.setDireccionDestino(rs.getString("direccion_destino"));
        envio.setFechaEnvio(rs.getDate("fecha_envio").toLocalDate());
        Date fechaEntregaFecha = rs.getDate("fecha_entrega");
        envio.setFechaEntrega(fechaEntregaFecha == null ? null : fechaEntregaFecha.toLocalDate());
        envio.setCostoEnvio(rs.getDouble("costo_envio"));
        String estadoEnvioTexto = rs.getString("estado_envio");
        envio.setEstadoEnvio(estadoEnvioTexto == null ? null : EstadoEnvio.valueOf(estadoEnvioTexto));

        return envio;
    }

}
