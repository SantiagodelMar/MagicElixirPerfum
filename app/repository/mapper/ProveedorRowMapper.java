package repository.mapper;

import domain.Proveedor;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProveedorRowMapper implements RowMapper<Proveedor> {

    @Override
    public Proveedor mapRow(ResultSet rs) throws SQLException {

        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(rs.getInt("id_proveedor"));
        proveedor.setNombre(rs.getString("nombre"));
        proveedor.setDocumento(rs.getString("documento"));
        proveedor.setTelefono(rs.getString("telefono"));
        proveedor.setCorreo(rs.getString("correo"));
        proveedor.setDireccion(rs.getString("direccion"));
        proveedor.setActivo(rs.getBoolean("activo"));

        return proveedor;
    }

}
