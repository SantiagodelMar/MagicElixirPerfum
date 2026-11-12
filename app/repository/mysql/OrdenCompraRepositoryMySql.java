package repository.mysql;

import domain.OrdenCompra;
import domain.Proveedor;
import domain.enums.EstadoOrdenCompra;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.OrdenCompraRowMapper;
import service.outputport.OrdenCompraRepositoryPort;
import service.outputport.ProveedorRepositoryPort;

public class OrdenCompraRepositoryMySql implements OrdenCompraRepositoryPort {

    private final Connection connection;
    private final OrdenCompraRowMapper rowMapper;
    private final ProveedorRepositoryPort proveedorRepository;

    public OrdenCompraRepositoryMySql(Connection connection, OrdenCompraRowMapper rowMapper, ProveedorRepositoryPort proveedorRepository) {
        this.connection = connection;
        this.rowMapper = rowMapper;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public OrdenCompra createOrdenCompra(OrdenCompra ordenCompra) {
        String sql = "INSERT INTO orden_compra (id_orden_compra, id_proveedor, fecha_orden, estado, total, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ordenCompra.getIdOrdenCompra());
            ps.setInt(2, ordenCompra.getProveedor().getIdProveedor());
            ps.setDate(3, Date.valueOf(ordenCompra.getFechaOrden()));
            ps.setString(4, ordenCompra.getEstado() == null ? null : ordenCompra.getEstado().name());
            ps.setDouble(5, ordenCompra.getTotal());
            ps.setString(6, ordenCompra.getObservaciones());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar ordencompra", e);
        }
        return ordenCompra;
    }

    @Override
    public OrdenCompra selectOrdenCompraById(int id) {
        String sql = "SELECT * FROM orden_compra WHERE id_orden_compra = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar ordencompra por id", e);
        }
        return null;
    }

    @Override
    public List<OrdenCompra> selectAllOrdenesCompra() {
        List<OrdenCompra> lista = new ArrayList<>();
        String sql = "SELECT * FROM orden_compra ORDER BY id_orden_compra";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(hidratar(rowMapper.mapRow(rs)));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar ordencompra", e);
        }
        return lista;
    }

    @Override
    public OrdenCompra updateOrdenCompra(OrdenCompra ordenCompra) {
        String sql = "UPDATE orden_compra SET id_proveedor = ?, fecha_orden = ?, estado = ?, total = ?, observaciones = ? WHERE id_orden_compra = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ordenCompra.getProveedor().getIdProveedor());
            ps.setDate(2, Date.valueOf(ordenCompra.getFechaOrden()));
            ps.setString(3, ordenCompra.getEstado() == null ? null : ordenCompra.getEstado().name());
            ps.setDouble(4, ordenCompra.getTotal());
            ps.setString(5, ordenCompra.getObservaciones());
            ps.setInt(6, ordenCompra.getIdOrdenCompra());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar ordencompra", e);
        }
        return ordenCompra;
    }

    @Override
    public void deleteOrdenCompraById(int id) {
        String sql = "DELETE FROM orden_compra WHERE id_orden_compra = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar ordencompra", e);
        }
    }

    // El mapper solo trae el id de la relacion; aqui se busca el objeto completo
    private OrdenCompra hidratar(OrdenCompra ordenCompra) {
        ordenCompra.setProveedor(proveedorRepository.selectProveedorById(ordenCompra.getProveedor().getIdProveedor()));
        return ordenCompra;
    }

}
