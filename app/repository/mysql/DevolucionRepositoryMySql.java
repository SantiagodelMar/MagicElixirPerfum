package repository.mysql;

import domain.Devolucion;
import domain.enums.EstadoDevolucion;
import domain.enums.MotivoDevolucion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.DevolucionRowMapper;
import service.outputport.DevolucionRepositoryPort;

public class DevolucionRepositoryMySql implements DevolucionRepositoryPort {

    private final Connection connection;
    private final DevolucionRowMapper rowMapper;

    public DevolucionRepositoryMySql(Connection connection, DevolucionRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public Devolucion createDevolucion(Devolucion devolucion) {
        String sql = "INSERT INTO devolucion (id_devolucion, id_pedido, fecha_entrega, fecha_solicitud, motivo, descripcion, total_pagado, estado, monto_reembolso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, devolucion.getIdDevolucion());
            ps.setInt(2, devolucion.getIdPedido());
            ps.setDate(3, Date.valueOf(devolucion.getFechaEntrega()));
            ps.setDate(4, Date.valueOf(devolucion.getFechaSolicitud()));
            ps.setString(5, devolucion.getMotivo() == null ? null : devolucion.getMotivo().name());
            ps.setString(6, devolucion.getDescripcion());
            ps.setDouble(7, devolucion.getTotalPagado());
            ps.setString(8, devolucion.getEstado() == null ? null : devolucion.getEstado().name());
            ps.setDouble(9, devolucion.getMontoReembolso());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar devolucion", e);
        }
        return devolucion;
    }

    @Override
    public Devolucion selectDevolucionById(int id) {
        String sql = "SELECT * FROM devolucion WHERE id_devolucion = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar devolucion por id", e);
        }
        return null;
    }

    @Override
    public List<Devolucion> selectAllDevoluciones() {
        List<Devolucion> lista = new ArrayList<>();
        String sql = "SELECT * FROM devolucion ORDER BY id_devolucion";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar devolucion", e);
        }
        return lista;
    }

    @Override
    public Devolucion updateDevolucion(Devolucion devolucion) {
        String sql = "UPDATE devolucion SET id_pedido = ?, fecha_entrega = ?, fecha_solicitud = ?, motivo = ?, descripcion = ?, total_pagado = ?, estado = ?, monto_reembolso = ? WHERE id_devolucion = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, devolucion.getIdPedido());
            ps.setDate(2, Date.valueOf(devolucion.getFechaEntrega()));
            ps.setDate(3, Date.valueOf(devolucion.getFechaSolicitud()));
            ps.setString(4, devolucion.getMotivo() == null ? null : devolucion.getMotivo().name());
            ps.setString(5, devolucion.getDescripcion());
            ps.setDouble(6, devolucion.getTotalPagado());
            ps.setString(7, devolucion.getEstado() == null ? null : devolucion.getEstado().name());
            ps.setDouble(8, devolucion.getMontoReembolso());
            ps.setInt(9, devolucion.getIdDevolucion());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar devolucion", e);
        }
        return devolucion;
    }

    @Override
    public void deleteDevolucionById(int id) {
        String sql = "DELETE FROM devolucion WHERE id_devolucion = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar devolucion", e);
        }
    }

}
