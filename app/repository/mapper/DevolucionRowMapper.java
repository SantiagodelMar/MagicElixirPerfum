package repository.mapper;

import domain.Devolucion;
import domain.enums.EstadoDevolucion;
import domain.enums.MotivoDevolucion;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DevolucionRowMapper implements RowMapper<Devolucion> {

    @Override
    public Devolucion mapRow(ResultSet rs) throws SQLException {

        Devolucion devolucion = new Devolucion();
        devolucion.setIdDevolucion(rs.getInt("id_devolucion"));
        devolucion.setIdPedido(rs.getInt("id_pedido"));
        devolucion.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
        devolucion.setFechaSolicitud(rs.getDate("fecha_solicitud").toLocalDate());
        String motivoTexto = rs.getString("motivo");
        devolucion.setMotivo(motivoTexto == null ? null : MotivoDevolucion.valueOf(motivoTexto));
        devolucion.setDescripcion(rs.getString("descripcion"));
        devolucion.setTotalPagado(rs.getDouble("total_pagado"));
        String estadoTexto = rs.getString("estado");
        devolucion.setEstado(estadoTexto == null ? null : EstadoDevolucion.valueOf(estadoTexto));
        devolucion.setMontoReembolso(rs.getDouble("monto_reembolso"));

        return devolucion;
    }

}
