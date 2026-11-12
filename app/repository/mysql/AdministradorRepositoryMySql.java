package repository.mysql;

import domain.Administrador;
import domain.enums.TipoDocumento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.AdministradorRowMapper;
import service.outputport.AdministradorRepositoryPort;

public class AdministradorRepositoryMySql implements AdministradorRepositoryPort {

    private final Connection connection;
    private final AdministradorRowMapper rowMapper;

    public AdministradorRepositoryMySql(Connection connection, AdministradorRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public Administrador createAdministrador(Administrador administrador) {
        String sql = "INSERT INTO administrador (id_usuario, nombre, apellido, correo, tipo_doc, celular, direccion, fecha_nacimiento, permisos, nivel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, administrador.getIdUsuario());
            ps.setString(2, administrador.getNombre());
            ps.setString(3, administrador.getApellido());
            ps.setString(4, administrador.getCorreo());
            ps.setString(5, administrador.getTipoDoc() == null ? null : administrador.getTipoDoc().name());
            ps.setString(6, administrador.getCelular());
            ps.setString(7, administrador.getDireccion());
            ps.setString(8, administrador.getFechaNacimiento());
            ps.setString(9, administrador.getPermissions());
            ps.setString(10, administrador.getLevel());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar administrador", e);
        }
        return administrador;
    }

    @Override
    public Administrador selectAdministradorById(int id) {
        String sql = "SELECT * FROM administrador WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar administrador por id", e);
        }
        return null;
    }

    @Override
    public List<Administrador> selectAllAdministradores() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM administrador ORDER BY id_usuario";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar administrador", e);
        }
        return lista;
    }

    @Override
    public Administrador updateAdministrador(Administrador administrador) {
        String sql = "UPDATE administrador SET nombre = ?, apellido = ?, correo = ?, tipo_doc = ?, celular = ?, direccion = ?, fecha_nacimiento = ?, permisos = ?, nivel = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, administrador.getNombre());
            ps.setString(2, administrador.getApellido());
            ps.setString(3, administrador.getCorreo());
            ps.setString(4, administrador.getTipoDoc() == null ? null : administrador.getTipoDoc().name());
            ps.setString(5, administrador.getCelular());
            ps.setString(6, administrador.getDireccion());
            ps.setString(7, administrador.getFechaNacimiento());
            ps.setString(8, administrador.getPermissions());
            ps.setString(9, administrador.getLevel());
            ps.setInt(10, administrador.getIdUsuario());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar administrador", e);
        }
        return administrador;
    }

    @Override
    public void deleteAdministradorById(int id) {
        String sql = "DELETE FROM administrador WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar administrador", e);
        }
    }

}
