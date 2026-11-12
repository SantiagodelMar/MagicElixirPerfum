package repository.mysql;

import domain.Inventario;
import domain.Perfume;
import domain.enums.EstadoInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.InventarioRowMapper;
import service.outputport.InventarioRepositoryPort;
import service.outputport.PerfumeRepositoryPort;

public class InventarioRepositoryMySql implements InventarioRepositoryPort {

    private final Connection connection;
    private final InventarioRowMapper rowMapper;
    private final PerfumeRepositoryPort perfumeRepository;

    public InventarioRepositoryMySql(Connection connection, InventarioRowMapper rowMapper, PerfumeRepositoryPort perfumeRepository) {
        this.connection = connection;
        this.rowMapper = rowMapper;
        this.perfumeRepository = perfumeRepository;
    }

    @Override
    public Inventario createInventario(Inventario inventario) {
        String sql = "INSERT INTO inventario (id_inventario, id_perfume, stock_disponible, estado_disponibilidad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventario.getIdInventario());
            ps.setInt(2, inventario.getPerfume().getIdPerfume());
            ps.setInt(3, inventario.getStockDisponible());
            ps.setString(4, inventario.getEstadoDisponibilidad() == null ? null : inventario.getEstadoDisponibilidad().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar inventario", e);
        }
        return inventario;
    }

    @Override
    public Inventario selectInventarioById(int id) {
        String sql = "SELECT * FROM inventario WHERE id_inventario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar inventario por id", e);
        }
        return null;
    }

    @Override
    public Inventario selectInventarioByPerfume(int idPerfume) {
        String sql = "SELECT * FROM inventario WHERE id_perfume = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPerfume);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar el inventario del perfume", e);
        }
        return null;
    }

    @Override
    public List<Inventario> selectAllInventarios() {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY id_inventario";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(hidratar(rowMapper.mapRow(rs)));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar inventario", e);
        }
        return lista;
    }

    @Override
    public Inventario updateInventario(Inventario inventario) {
        String sql = "UPDATE inventario SET id_perfume = ?, stock_disponible = ?, estado_disponibilidad = ? WHERE id_inventario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, inventario.getPerfume().getIdPerfume());
            ps.setInt(2, inventario.getStockDisponible());
            ps.setString(3, inventario.getEstadoDisponibilidad() == null ? null : inventario.getEstadoDisponibilidad().name());
            ps.setInt(4, inventario.getIdInventario());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar inventario", e);
        }
        return inventario;
    }

    @Override
    public void deleteInventarioById(int id) {
        String sql = "DELETE FROM inventario WHERE id_inventario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar inventario", e);
        }
    }

    // El mapper solo trae el id de la relacion; aqui se busca el objeto completo
    private Inventario hidratar(Inventario inventario) {
        inventario.setPerfume(perfumeRepository.selectPerfumeById(inventario.getPerfume().getIdPerfume()));
        return inventario;
    }

}
