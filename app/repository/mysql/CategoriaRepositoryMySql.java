package repository.mysql;

import domain.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.CategoriaRowMapper;
import service.outputport.CategoriaRepositoryPort;

public class CategoriaRepositoryMySql implements CategoriaRepositoryPort {

    private final Connection connection;
    private final CategoriaRowMapper rowMapper;

    public CategoriaRepositoryMySql(Connection connection, CategoriaRowMapper rowMapper) {
        this.connection = connection;
        this.rowMapper = rowMapper;
    }

    @Override
    public Categoria createCategoria(Categoria categoria) {
        String sql = "INSERT INTO categoria (id_categoria, nombre, descripcion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoria.getIdCategoria());
            ps.setString(2, categoria.getNombre());
            ps.setString(3, categoria.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar categoria", e);
        }
        return categoria;
    }

    @Override
    public Categoria selectCategoriaById(int id) {
        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar categoria por id", e);
        }
        return null;
    }

    @Override
    public List<Categoria> selectAllCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria ORDER BY id_categoria";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar categoria", e);
        }
        return lista;
    }

    @Override
    public Categoria updateCategoria(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, categoria.getIdCategoria());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar categoria", e);
        }
        return categoria;
    }

    @Override
    public void deleteCategoriaById(int id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar categoria", e);
        }
    }

}
