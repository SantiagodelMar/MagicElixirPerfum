package repository.mapper;

import domain.Empleado;
import domain.enums.TipoDocumento;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoRowMapper implements RowMapper<Empleado> {

    @Override
    public Empleado mapRow(ResultSet rs) throws SQLException {

        Empleado empleado = new Empleado();
        empleado.setIdUsuario(rs.getInt("id_usuario"));
        empleado.setNombre(rs.getString("nombre"));
        empleado.setApellido(rs.getString("apellido"));
        empleado.setCorreo(rs.getString("correo"));
        String tipoDocTexto = rs.getString("tipo_doc");
        empleado.setTipoDoc(tipoDocTexto == null ? null : TipoDocumento.valueOf(tipoDocTexto));
        empleado.setCelular(rs.getString("celular"));
        empleado.setDireccion(rs.getString("direccion"));
        empleado.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        empleado.setRole(rs.getString("rol"));
        empleado.setPassword(rs.getString("clave"));
        empleado.setActivo(rs.getBoolean("activo"));

        return empleado;
    }

}
