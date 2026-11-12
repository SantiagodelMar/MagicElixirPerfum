package repository.mysql;

import domain.Categoria;
import domain.Perfume;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.PerfumeRowMapper;
import service.outputport.CategoriaRepositoryPort;
import service.outputport.PerfumeRepositoryPort;

public class PerfumeRepositoryMySql implements PerfumeRepositoryPort {

    private final Connection connection;
    private final PerfumeRowMapper rowMapper;
    private final CategoriaRepositoryPort categoriaRepository;

    public PerfumeRepositoryMySql(Connection connection, PerfumeRowMapper rowMapper, CategoriaRepositoryPort categoriaRepository) {
        this.connection = connection;
        this.rowMapper = rowMapper;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Perfume createPerfume(Perfume perfume) {
        String sql = "INSERT INTO perfume (id_perfume, nombre, marca, precio, mililitros, notas_olfativas, id_categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, perfume.getIdPerfume());
            ps.setString(2, perfume.getNombre());
            ps.setString(3, perfume.getMarca());
            ps.setDouble(4, perfume.getPrecio());
            ps.setInt(5, perfume.getMililitros());
            ps.setString(6, perfume.getNotasOlfativas());
            ps.setInt(7, perfume.getCategoria().getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar perfume", e);
        }
        return perfume;
    }

    @Override
    public Perfume selectPerfumeById(int id) {
        String sql = "SELECT * FROM perfume WHERE id_perfume = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar perfume por id", e);
        }
        return null;
    }

    @Override
    public List<Perfume> selectAllPerfumes() {
        List<Perfume> lista = new ArrayList<>();
        String sql = "SELECT * FROM perfume ORDER BY id_perfume";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(hidratar(rowMapper.mapRow(rs)));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar perfume", e);
        }
        return lista;
    }

    @Override
    public Perfume updatePerfume(Perfume perfume) {
        String sql = "UPDATE perfume SET nombre = ?, marca = ?, precio = ?, mililitros = ?, notas_olfativas = ?, id_categoria = ? WHERE id_perfume = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, perfume.getNombre());
            ps.setString(2, perfume.getMarca());
            ps.setDouble(3, perfume.getPrecio());
            ps.setInt(4, perfume.getMililitros());
            ps.setString(5, perfume.getNotasOlfativas());
            ps.setInt(6, perfume.getCategoria().getIdCategoria());
            ps.setInt(7, perfume.getIdPerfume());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar perfume", e);
        }
        return perfume;
    }

    @Override
    public void deletePerfumeById(int id) {
        String sql = "DELETE FROM perfume WHERE id_perfume = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar perfume", e);
        }
    }

    // El mapper solo trae el id de la relacion; aqui se busca el objeto completo
    private Perfume hidratar(Perfume perfume) {
        perfume.setCategoria(categoriaRepository.selectCategoriaById(perfume.getCategoria().getIdCategoria()));
        return perfume;
    }

}
