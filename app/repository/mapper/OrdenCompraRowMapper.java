package repository.mapper;

import domain.OrdenCompra;
import domain.Proveedor;
import domain.enums.EstadoOrdenCompra;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdenCompraRowMapper implements RowMapper<OrdenCompra> {

    @Override
    public OrdenCompra mapRow(ResultSet rs) throws SQLException {

        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setIdOrdenCompra(rs.getInt("id_orden_compra"));
        // Solo se guarda el id; el repository trae el objeto completo (ver hidratar)
        Proveedor proveedorRef = new Proveedor();
        proveedorRef.setIdProveedor(rs.getInt("id_proveedor"));
        ordenCompra.setProveedor(proveedorRef);
        ordenCompra.setFechaOrden(rs.getDate("fecha_orden").toLocalDate());
        String estadoTexto = rs.getString("estado");
        ordenCompra.setEstado(estadoTexto == null ? null : EstadoOrdenCompra.valueOf(estadoTexto));
        ordenCompra.setTotal(rs.getDouble("total"));
        ordenCompra.setObservaciones(rs.getString("observaciones"));

        return ordenCompra;
    }

}
