package repository.mysql;

import domain.Garantia;
import domain.Pedido;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.GarantiaRowMapper;
import service.outputport.GarantiaRepositoryPort;
import service.outputport.PedidoRepositoryPort;

public class GarantiaRepositoryMySql implements GarantiaRepositoryPort {

    private final Connection connection;
    private final GarantiaRowMapper rowMapper;
    private final PedidoRepositoryPort pedidoRepository;

    public GarantiaRepositoryMySql(Connection connection, GarantiaRowMapper rowMapper, PedidoRepositoryPort pedidoRepository) {
        this.connection = connection;
        this.rowMapper = rowMapper;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Garantia createGarantia(Garantia garantia) {
        String sql = "INSERT INTO garantia (id_garantia, id_pedido, fecha_inicio, meses_cobertura, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, garantia.getIdGarantia());
            ps.setInt(2, garantia.getPedido().getIdPedido());
            ps.setDate(3, Date.valueOf(garantia.getFechaInicio()));
            ps.setInt(4, garantia.getMesesCobertura());
            ps.setString(5, garantia.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar garantia", e);
        }
        return garantia;
    }

    @Override
    public Garantia selectGarantiaById(int id) {
        String sql = "SELECT * FROM garantia WHERE id_garantia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar garantia por id", e);
        }
        return null;
    }

    @Override
    public List<Garantia> selectAllGarantias() {
        List<Garantia> lista = new ArrayList<>();
        String sql = "SELECT * FROM garantia ORDER BY id_garantia";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(hidratar(rowMapper.mapRow(rs)));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar garantia", e);
        }
        return lista;
    }

    @Override
    public Garantia updateGarantia(Garantia garantia) {
        String sql = "UPDATE garantia SET id_pedido = ?, fecha_inicio = ?, meses_cobertura = ?, descripcion = ? WHERE id_garantia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, garantia.getPedido().getIdPedido());
            ps.setDate(2, Date.valueOf(garantia.getFechaInicio()));
            ps.setInt(3, garantia.getMesesCobertura());
            ps.setString(4, garantia.getDescripcion());
            ps.setInt(5, garantia.getIdGarantia());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar garantia", e);
        }
        return garantia;
    }

    @Override
    public void deleteGarantiaById(int id) {
        String sql = "DELETE FROM garantia WHERE id_garantia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar garantia", e);
        }
    }

    // El mapper solo trae el id de la relacion; aqui se busca el objeto completo
    private Garantia hidratar(Garantia garantia) {
        garantia.setPedido(pedidoRepository.selectPedidoById(garantia.getPedido().getIdPedido()));
        return garantia;
    }

}
