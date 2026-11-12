package repository.mapper;

import domain.Inventario;
import domain.Perfume;
import domain.enums.EstadoInventario;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventarioRowMapper implements RowMapper<Inventario> {

    @Override
    public Inventario mapRow(ResultSet rs) throws SQLException {

        Inventario inventario = new Inventario();
        inventario.setIdInventario(rs.getInt("id_inventario"));
        // Solo se guarda el id; el repository trae el objeto completo (ver hidratar)
        Perfume perfumeRef = new Perfume();
        perfumeRef.setIdPerfume(rs.getInt("id_perfume"));
        inventario.setPerfume(perfumeRef);
        inventario.setStockDisponible(rs.getInt("stock_disponible"));
        String estadoDisponibilidadTexto = rs.getString("estado_disponibilidad");
        inventario.setEstadoDisponibilidad(estadoDisponibilidadTexto == null ? null : EstadoInventario.valueOf(estadoDisponibilidadTexto));

        return inventario;
    }

}
