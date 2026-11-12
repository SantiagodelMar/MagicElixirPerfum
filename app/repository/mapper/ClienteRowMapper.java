package repository.mapper;

import domain.Cliente;
import domain.enums.TipoDocumento;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteRowMapper implements RowMapper<Cliente> {

    @Override
    public Cliente mapRow(ResultSet rs) throws SQLException {

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(rs.getInt("id_usuario"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellido(rs.getString("apellido"));
        cliente.setCorreo(rs.getString("correo"));
        String tipoDocTexto = rs.getString("tipo_doc");
        cliente.setTipoDoc(tipoDocTexto == null ? null : TipoDocumento.valueOf(tipoDocTexto));
        cliente.setCelular(rs.getString("celular"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        cliente.setTipoCliente(rs.getString("tipo_cliente"));
        cliente.setDireccionEnvio(rs.getString("direccion_envio"));

        return cliente;
    }

}
