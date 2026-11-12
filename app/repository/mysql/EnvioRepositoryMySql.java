package repository.mysql;

import domain.Envio;
import domain.Pedido;
import domain.enums.EstadoEnvio;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import repository.mapper.EnvioRowMapper;
import service.outputport.EnvioRepositoryPort;
import service.outputport.PedidoRepositoryPort;

public class EnvioRepositoryMySql implements EnvioRepositoryPort {

    private final Connection connection;
    private final EnvioRowMapper rowMapper;
    private final PedidoRepositoryPort pedidoRepository;

    public EnvioRepositoryMySql(Connection connection, EnvioRowMapper rowMapper, PedidoRepositoryPort pedidoRepository) {
        this.connection = connection;
        this.rowMapper = rowMapper;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Envio createEnvio(Envio envio) {
        String sql = "INSERT INTO envio (id_envio, id_pedido, transportadora, numero_guia, direccion_destino, fecha_envio, fecha_entrega, costo_envio, estado_envio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, envio.getIdEnvio());
            ps.setInt(2, envio.getPedido().getIdPedido());
            ps.setString(3, envio.getTransportadora());
            ps.setString(4, envio.getNumeroDeGuia());
            ps.setString(5, envio.getDireccionDestino());
            ps.setDate(6, Date.valueOf(envio.getFechaEnvio()));
            ps.setDate(7, envio.getFechaEntrega() == null ? null : Date.valueOf(envio.getFechaEntrega()));
            ps.setDouble(8, envio.getCostoEnvio());
            ps.setString(9, envio.getEstadoEnvio() == null ? null : envio.getEstadoEnvio().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("guardar envio", e);
        }
        return envio;
    }

    @Override
    public Envio selectEnvioById(int id) {
        String sql = "SELECT * FROM envio WHERE id_envio = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar envio por id", e);
        }
        return null;
    }

    @Override
    public Envio selectEnvioByGuia(String numeroDeGuia) {
        String sql = "SELECT * FROM envio WHERE numero_guia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, numeroDeGuia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar el envio por guia", e);
        }
        return null;
    }

    @Override
    public List<Envio> selectAllEnvios() {
        List<Envio> lista = new ArrayList<>();
        String sql = "SELECT * FROM envio ORDER BY id_envio";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(hidratar(rowMapper.mapRow(rs)));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar envio", e);
        }
        return lista;
    }

    @Override
    public Envio updateEnvio(Envio envio) {
        String sql = "UPDATE envio SET id_pedido = ?, transportadora = ?, numero_guia = ?, direccion_destino = ?, fecha_envio = ?, fecha_entrega = ?, costo_envio = ?, estado_envio = ? WHERE id_envio = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, envio.getPedido().getIdPedido());
            ps.setString(2, envio.getTransportadora());
            ps.setString(3, envio.getNumeroDeGuia());
            ps.setString(4, envio.getDireccionDestino());
            ps.setDate(5, Date.valueOf(envio.getFechaEnvio()));
            ps.setDate(6, envio.getFechaEntrega() == null ? null : Date.valueOf(envio.getFechaEntrega()));
            ps.setDouble(7, envio.getCostoEnvio());
            ps.setString(8, envio.getEstadoEnvio() == null ? null : envio.getEstadoEnvio().name());
            ps.setInt(9, envio.getIdEnvio());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("actualizar envio", e);
        }
        return envio;
    }

    @Override
    public void deleteEnvioById(int id) {
        String sql = "DELETE FROM envio WHERE id_envio = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar envio", e);
        }
    }

    // El mapper solo trae el id de la relacion; aqui se busca el objeto completo
    private Envio hidratar(Envio envio) {
        envio.setPedido(pedidoRepository.selectPedidoById(envio.getPedido().getIdPedido()));
        return envio;
    }

}
