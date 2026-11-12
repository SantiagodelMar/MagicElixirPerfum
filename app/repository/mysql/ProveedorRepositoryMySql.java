package repository.mysql;

import domain.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.ProveedorRowMapper;
import service.outputport.ProveedorRepositoryPort;

public class ProveedorRepositoryMySql implements ProveedorRepositoryPort {

    private final Connection connection;
    private final ProveedorRowMapper rowMapper;

    public ProveedorRepositoryMySql(Connection connection, ProveedorRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public Proveedor createProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO proveedor (id_proveedor, nombre, documento, telefono, correo, direccion, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, proveedor.getIdProveedor());
            ps.setString(2, proveedor.getNombre());
            ps.setString(3, proveedor.getDocumento());
            ps.setString(4, proveedor.getTelefono());
            ps.setString(5, proveedor.getCorreo());
            ps.setString(6, proveedor.getDireccion());
            ps.setBoolean(7, proveedor.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar proveedor", e);
        }
        return proveedor;
    }

    @Override
    public Proveedor selectProveedorById(int id) {
        String sql = "SELECT * FROM proveedor WHERE id_proveedor = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar proveedor por id", e);
        }
        return null;
    }

    @Override
    public List<Proveedor> selectAllProveedores() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor ORDER BY id_proveedor";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar proveedor", e);
        }
        return lista;
    }

    @Override
    public Proveedor updateProveedor(Proveedor proveedor) {
        String sql = "UPDATE proveedor SET nombre = ?, documento = ?, telefono = ?, correo = ?, direccion = ?, activo = ? WHERE id_proveedor = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getDocumento());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getCorreo());
            ps.setString(5, proveedor.getDireccion());
            ps.setBoolean(6, proveedor.isActivo());
            ps.setInt(7, proveedor.getIdProveedor());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar proveedor", e);
        }
        return proveedor;
    }

    @Override
    public void deleteProveedorById(int id) {
        String sql = "DELETE FROM proveedor WHERE id_proveedor = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar proveedor", e);
        }
    }

}
