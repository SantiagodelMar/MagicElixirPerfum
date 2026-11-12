package repository.mysql;

import domain.Cliente;
import domain.DetallePedido;
import domain.Perfume;
import domain.Pedido;
import repository.mapper.PedidoRowMapper;
import service.outputport.ClienteRepositoryPort;
import service.outputport.PedidoRepositoryPort;
import service.outputport.PerfumeRepositoryPort;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Un pedido vive en DOS tablas: pedido (los datos generales) y detalle_pedido (los perfumes que lleva).
// Por eso guardar y actualizar usan una TRANSACCION: o se guardan las dos o no se guarda ninguna.
public class PedidoRepositoryMySql implements PedidoRepositoryPort {

    private final Connection connection;
    private final PedidoRowMapper rowMapper;
    private final ClienteRepositoryPort clienteRepository;
    private final PerfumeRepositoryPort perfumeRepository;

    public PedidoRepositoryMySql(Connection connection, PedidoRowMapper rowMapper,
                                 ClienteRepositoryPort clienteRepository, PerfumeRepositoryPort perfumeRepository) {
        this.connection = connection;
        this.rowMapper = rowMapper;
        this.clienteRepository = clienteRepository;
        this.perfumeRepository = perfumeRepository;
    }

    @Override
    public Pedido createPedido(Pedido pedido) {
        String sql = "INSERT INTO pedido (id_pedido, fecha_pedido, id_cliente, total, observaciones, "
                + "estado_pedido, metodo_pago, canal_venta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, pedido.getIdPedido());
                ps.setDate(2, Date.valueOf(pedido.getFechaPedido()));
                ps.setInt(3, pedido.getCliente().getIdUsuario());
                ps.setDouble(4, pedido.getTotalCostoPedido());
                ps.setString(5, pedido.getObservaciones());
                ps.setString(6, pedido.getEstadoPedido().name());
                ps.setString(7, pedido.getMetodoPago().name());
                ps.setString(8, pedido.getCanalVenta() == null ? null : pedido.getCanalVenta().name());
                ps.executeUpdate();
            }
            guardarDetalles(pedido);
            connection.commit();
        } catch (SQLException e) {
            deshacer();
            throw ErroresBD.traducir("guardar el pedido", e);
        } finally {
            volverAutoCommit();
        }
        return pedido;
    }

    @Override
    public Pedido selectPedidoById(int id) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return hidratar(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("buscar el pedido por id", e);
        }
        return null;
    }

    @Override
    public List<Pedido> selectAllPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido ORDER BY id_pedido";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pedidos.add(hidratar(rowMapper.mapRow(rs)));
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("listar los pedidos", e);
        }
        return pedidos;
    }

    // Se actualiza la fila del pedido y se vuelven a escribir sus detalles
    @Override
    public Pedido updatePedido(Pedido pedido) {
        String sql = "UPDATE pedido SET fecha_pedido = ?, id_cliente = ?, total = ?, observaciones = ?, "
                + "estado_pedido = ?, metodo_pago = ?, canal_venta = ? WHERE id_pedido = ?";
        try {
            connection.setAutoCommit(false);
            int filas;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(pedido.getFechaPedido()));
                ps.setInt(2, pedido.getCliente().getIdUsuario());
                ps.setDouble(3, pedido.getTotalCostoPedido());
                ps.setString(4, pedido.getObservaciones());
                ps.setString(5, pedido.getEstadoPedido().name());
                ps.setString(6, pedido.getMetodoPago().name());
                ps.setString(7, pedido.getCanalVenta() == null ? null : pedido.getCanalVenta().name());
                ps.setInt(8, pedido.getIdPedido());
                filas = ps.executeUpdate();
            }
            if (filas == 0) {
                connection.rollback();
                return null;
            }
            try (PreparedStatement borrar = connection.prepareStatement("DELETE FROM detalle_pedido WHERE id_pedido = ?")) {
                borrar.setInt(1, pedido.getIdPedido());
                borrar.executeUpdate();
            }
            guardarDetalles(pedido);
            connection.commit();
        } catch (SQLException e) {
            deshacer();
            throw ErroresBD.traducir("actualizar el pedido", e);
        } finally {
            volverAutoCommit();
        }
        return pedido;
    }

    // Los detalles se borran solos (ON DELETE CASCADE en la base de datos)
    @Override
    public void deletePedidoById(int id) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ErroresBD.traducir("eliminar el pedido", e);
        }
    }

    // --- metodos de ayuda ---

    private void guardarDetalles(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (id_pedido, id_detalle, id_perfume, cantidad, precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (DetallePedido d : pedido.getDetalles()) {
                ps.setInt(1, pedido.getIdPedido());
                ps.setInt(2, d.getIdDetallePedido());
                ps.setInt(3, d.getPerfume().getIdPerfume());
                ps.setInt(4, d.getCantidad());
                ps.setDouble(5, d.getPrecioUnitario());
                ps.setDouble(6, d.getSubtotal());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // Completa el pedido: trae el cliente completo y la lista de detalles con cada perfume
    private Pedido hidratar(Pedido pedido) {
        Cliente cliente = clienteRepository.selectClienteById(pedido.getCliente().getIdUsuario());
        pedido.setCliente(cliente);
        pedido.setDetalles(cargarDetalles(pedido.getIdPedido()));
        return pedido;
    }

    private List<DetallePedido> cargarDetalles(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido WHERE id_pedido = ? ORDER BY id_detalle";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido d = new DetallePedido();
                    d.setIdDetallePedido(rs.getInt("id_detalle"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    Perfume perfume = perfumeRepository.selectPerfumeById(rs.getInt("id_perfume"));
                    d.setPerfume(perfume);
                    detalles.add(d);
                }
            }
        } catch (SQLException e) {
            throw ErroresBD.traducir("cargar el detalle del pedido", e);
        }
        return detalles;
    }

    private void deshacer() {
        try {
            connection.rollback();
        } catch (SQLException ignorado) {
            // si falla el rollback no hay mas que hacer
        }
    }

    private void volverAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ignorado) {
            // la conexion ya estaba cerrada
        }
    }

}
