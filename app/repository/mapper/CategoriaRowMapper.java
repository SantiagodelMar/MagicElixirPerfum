package repository.mapper;

import domain.Categoria;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaRowMapper implements RowMapper<Categoria> {

    @Override
    public Categoria mapRow(ResultSet rs) throws SQLException {

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setDescripcion(rs.getString("descripcion"));

        return categoria;
    }

}
