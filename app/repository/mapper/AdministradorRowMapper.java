package repository.mapper;

import domain.Administrador;
import domain.enums.TipoDocumento;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministradorRowMapper implements RowMapper<Administrador> {

    @Override
    public Administrador mapRow(ResultSet rs) throws SQLException {

        Administrador administrador = new Administrador();
        administrador.setIdUsuario(rs.getInt("id_usuario"));
        administrador.setNombre(rs.getString("nombre"));
        administrador.setApellido(rs.getString("apellido"));
        administrador.setCorreo(rs.getString("correo"));
        String tipoDocTexto = rs.getString("tipo_doc");
        administrador.setTipoDoc(tipoDocTexto == null ? null : TipoDocumento.valueOf(tipoDocTexto));
        administrador.setCelular(rs.getString("celular"));
        administrador.setDireccion(rs.getString("direccion"));
        administrador.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        administrador.setPermissions(rs.getString("permisos"));
        administrador.setLevel(rs.getString("nivel"));

        return administrador;
    }

}
