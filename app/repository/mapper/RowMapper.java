package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// Convierte UNA fila de la base de datos (ResultSet) en un objeto del dominio
public interface RowMapper<T> {

    T mapRow(ResultSet rs) throws SQLException;

}
