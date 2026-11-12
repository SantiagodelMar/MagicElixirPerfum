package repository.mapper;

import domain.Garantia;
import domain.Pedido;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GarantiaRowMapper implements RowMapper<Garantia> {

    @Override
    public Garantia mapRow(ResultSet rs) throws SQLException {

        Garantia garantia = new Garantia();
        garantia.setIdGarantia(rs.getInt("id_garantia"));
        // Solo se guarda el id; el repository trae el objeto completo (ver hidratar)
        Pedido pedidoRef = new Pedido();
        pedidoRef.setIdPedido(rs.getInt("id_pedido"));
        garantia.setPedido(pedidoRef);
        garantia.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        garantia.setMesesCobertura(rs.getInt("meses_cobertura"));
        garantia.setDescripcion(rs.getString("descripcion"));

        return garantia;
    }

}
