package repository.mysql;

import domain.Cliente;
import domain.enums.TipoDocumento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.ClienteRowMapper;
import service.outputport.ClienteRepositoryPort;

public class ClienteRepositoryMySql implements ClienteRepositoryPort {

    private final Connection connection;
    private final ClienteRowMapper rowMapper;

    public ClienteRepositoryMySql(Connection connection, ClienteRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public Cliente createCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (id_usuario, nombre, apellido, correo, tipo_doc, celular, direccion, fecha_nacimiento, tipo_cliente, direccion_envio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cliente.getIdUsuario());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getTipoDoc() == null ? null : cliente.getTipoDoc().name());
            ps.setString(6, cliente.getCelular());
            ps.setString(7, cliente.getDireccion());
            ps.setString(8, cliente.getFechaNacimiento());
            ps.setString(9, cliente.getTipoCliente());
            ps.setString(10, cliente.getDireccionEnvio());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar cliente", e);
        }
        return cliente;
    }

    @Override
    public Cliente selectClienteById(int id) {
        String sql = "SELECT * FROM cliente WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar cliente por id", e);
        }
        return null;
    }

    @Override
    public List<Cliente> selectAllClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY id_usuario";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar cliente", e);
        }
        return lista;
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre = ?, apellido = ?, correo = ?, tipo_doc = ?, celular = ?, direccion = ?, fecha_nacimiento = ?, tipo_cliente = ?, direccion_envio = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTipoDoc() == null ? null : cliente.getTipoDoc().name());
            ps.setString(5, cliente.getCelular());
            ps.setString(6, cliente.getDireccion());
            ps.setString(7, cliente.getFechaNacimiento());
            ps.setString(8, cliente.getTipoCliente());
            ps.setString(9, cliente.getDireccionEnvio());
            ps.setInt(10, cliente.getIdUsuario());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar cliente", e);
        }
        return cliente;
    }

    @Override
    public void deleteClienteById(int id) {
        String sql = "DELETE FROM cliente WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar cliente", e);
        }
    }

}
