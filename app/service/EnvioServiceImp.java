package service;

import domain.Envio;
import domain.Pedido;
import domain.enums.EstadoEnvio;
import domain.enums.EstadoPedido;
import service.inputport.EnvioServiceInterface;
import service.outputport.EnvioRepositoryPort;
import service.outputport.PedidoRepositoryPort;

import java.time.LocalDate;
import java.util.List;

public class EnvioServiceImp implements EnvioServiceInterface {

    private final EnvioRepositoryPort envioRepository;
    private final PedidoRepositoryPort pedidoRepository;

    public EnvioServiceImp(EnvioRepositoryPort envioRepository, PedidoRepositoryPort pedidoRepository) {
        this.envioRepository = envioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // HU-13: registrar el envio con guia y transportadora
    @Override
    public Envio createEnvio(int idEnvio, int idPedido, String transportadora, String numeroDeGuia,
                             String direccionDestino, double costoEnvio) {

        Pedido pedido = pedidoRepository.selectPedidoById(idPedido);
        if (pedido == null) {
            return null;
        }

        Envio envio = new Envio(idEnvio, pedido, transportadora, numeroDeGuia, direccionDestino,
                LocalDate.now(), costoEnvio, EstadoEnvio.CREADO);

        // El pedido pasa a enviado
        pedido.setEstadoPedido(EstadoPedido.ENVIADO);
        pedidoRepository.updatePedido(pedido);

        return envioRepository.createEnvio(envio);
    }

    @Override
    public Envio selectEnvioById(int id) {
        return envioRepository.selectEnvioById(id);
    }

    @Override
    public Envio selectEnvioByGuia(String numeroDeGuia) {
        return envioRepository.selectEnvioByGuia(numeroDeGuia);
    }

    @Override
    public List<Envio> selectAllEnvios() {
        return envioRepository.selectAllEnvios();
    }

    @Override
    public Envio updateEnvio(int idEnvio, String transportadora, String numeroDeGuia, String direccionDestino, double costoEnvio) {
        Envio envio = envioRepository.selectEnvioById(idEnvio);
        if (envio == null) {
            return null;
        }

        envio.setTransportadora(transportadora);
        envio.setNumeroDeGuia(numeroDeGuia);
        envio.setDireccionDestino(direccionDestino);
        envio.setCostoEnvio(costoEnvio);

        return envioRepository.updateEnvio(envio);
    }

    // HU-14: actualizar el estado para responder al cliente sin llamar
    @Override
    public Envio actualizarEstadoEnvio(int idEnvio, EstadoEnvio nuevoEstado) {
        Envio envio = envioRepository.selectEnvioById(idEnvio);
        if (envio == null) {
            return null;
        }

        envio.setEstadoEnvio(nuevoEstado);

        // Cuando se entrega se guarda la fecha y el pedido queda entregado
        if (nuevoEstado == EstadoEnvio.ENTREGADO) {
            envio.setFechaEntrega(LocalDate.now());
            envio.getPedido().setEstadoPedido(EstadoPedido.ENTREGADO);
            pedidoRepository.updatePedido(envio.getPedido());
        }

        return envioRepository.updateEnvio(envio);
    }

    @Override
    public void deleteEnvioById(int id) {
        envioRepository.deleteEnvioById(id);
    }

}
