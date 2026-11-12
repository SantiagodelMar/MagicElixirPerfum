package repository.mapper;

import domain.Categoria;
import domain.Perfume;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PerfumeRowMapper implements RowMapper<Perfume> {

    @Override
    public Perfume mapRow(ResultSet rs) throws SQLException {

        Perfume perfume = new Perfume();
        perfume.setIdPerfume(rs.getInt("id_perfume"));
        perfume.setNombre(rs.getString("nombre"));
        perfume.setMarca(rs.getString("marca"));
        perfume.setPrecio(rs.getDouble("precio"));
        perfume.setMililitros(rs.getInt("mililitros"));
        perfume.setNotasOlfativas(rs.getString("notas_olfativas"));
        // Solo se guarda el id; el repository trae el objeto completo (ver hidratar)
        Categoria categoriaRef = new Categoria();
        categoriaRef.setIdCategoria(rs.getInt("id_categoria"));
        perfume.setCategoria(categoriaRef);

        return perfume;
    }

}
