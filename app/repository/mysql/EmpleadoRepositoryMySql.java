package repository.mysql;

import domain.Empleado;
import domain.enums.TipoDocumento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.EmpleadoRowMapper;
import service.outputport.EmpleadoRepositoryPort;

public class EmpleadoRepositoryMySql implements EmpleadoRepositoryPort {

    private final Connection connection;
    private final EmpleadoRowMapper rowMapper;

    public EmpleadoRepositoryMySql(Connection connection, EmpleadoRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public Empleado createEmpleado(Empleado empleado) {
        String sql = "INSERT INTO empleado (id_usuario, nombre, apellido, correo, tipo_doc, celular, direccion, fecha_nacimiento, rol, clave, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, empleado.getIdUsuario());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getApellido());
            ps.setString(4, empleado.getCorreo());
            ps.setString(5, empleado.getTipoDoc() == null ? null : empleado.getTipoDoc().name());
            ps.setString(6, empleado.getCelular());
            ps.setString(7, empleado.getDireccion());
            ps.setString(8, empleado.getFechaNacimiento());
            ps.setString(9, empleado.getRole());
            ps.setString(10, empleado.getPassword());
            ps.setBoolean(11, empleado.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar empleado", e);
        }
        return empleado;
    }

    @Override
    public Empleado selectEmpleadoById(int id) {
        String sql = "SELECT * FROM empleado WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar empleado por id", e);
        }
        return null;
    }

    @Override
    public Empleado selectEmpleadoByCorreo(String correo) {
        String sql = "SELECT * FROM empleado WHERE LOWER(correo) = LOWER(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar el empleado por correo", e);
        }
        return null;
    }

    @Override
    public List<Empleado> selectAllEmpleados() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleado ORDER BY id_usuario";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar empleado", e);
        }
        return lista;
    }

    @Override
    public Empleado updateEmpleado(Empleado empleado) {
        String sql = "UPDATE empleado SET nombre = ?, apellido = ?, correo = ?, tipo_doc = ?, celular = ?, direccion = ?, fecha_nacimiento = ?, rol = ?, clave = ?, activo = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setString(3, empleado.getCorreo());
            ps.setString(4, empleado.getTipoDoc() == null ? null : empleado.getTipoDoc().name());
            ps.setString(5, empleado.getCelular());
            ps.setString(6, empleado.getDireccion());
            ps.setString(7, empleado.getFechaNacimiento());
            ps.setString(8, empleado.getRole());
            ps.setString(9, empleado.getPassword());
            ps.setBoolean(10, empleado.isActivo());
            ps.setInt(11, empleado.getIdUsuario());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar empleado", e);
        }
        return empleado;
    }

    @Override
    public void deleteEmpleadoById(int id) {
        String sql = "DELETE FROM empleado WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar empleado", e);
        }
    }

}
