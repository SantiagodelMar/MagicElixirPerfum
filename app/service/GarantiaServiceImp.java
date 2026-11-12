package service;

import domain.Garantia;
import domain.Pedido;
import service.inputport.GarantiaServiceInterface;
import service.outputport.GarantiaRepositoryPort;
import service.outputport.PedidoRepositoryPort;

import java.time.LocalDate;
import java.util.List;

public class GarantiaServiceImp implements GarantiaServiceInterface {

    private static final int MESES_MAXIMOS = 36;

    private final GarantiaRepositoryPort garantiaRepository;
    private final PedidoRepositoryPort pedidoRepository;

    public GarantiaServiceImp(GarantiaRepositoryPort garantiaRepository, PedidoRepositoryPort pedidoRepository) {
        this.garantiaRepository = garantiaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // HU-20: registrar la garantia de un pedido con fecha de inicio y meses de cobertura
    @Override
    public Garantia createGarantia(int idGarantia, int idPedido, LocalDate fechaInicio, int mesesCobertura, String descripcion) {
        Pedido pedido = pedidoRepository.selectPedidoById(idPedido);
        if (pedido == null) {
            return null;
        }
        if (mesesCobertura <= 0 || mesesCobertura > MESES_MAXIMOS) {
            return null;
        }
        if (fechaInicio == null || fechaInicio.isAfter(LocalDate.now())) {
            return null;
        }
        Garantia garantia = new Garantia(idGarantia, pedido, fechaInicio, mesesCobertura, descripcion);
        return garantiaRepository.createGarantia(garantia);
    }

    @Override
    public Garantia selectGarantiaById(int id) {
        return garantiaRepository.selectGarantiaById(id);
    }

    @Override
    public List<Garantia> selectAllGarantias() {
        return garantiaRepository.selectAllGarantias();
    }

    @Override
    public Garantia updateGarantia(int idGarantia, int mesesCobertura, String descripcion) {
        Garantia garantia = garantiaRepository.selectGarantiaById(idGarantia);
        if (garantia == null || mesesCobertura <= 0 || mesesCobertura > MESES_MAXIMOS) {
            return null;
        }
        garantia.setMesesCobertura(mesesCobertura);
        garantia.setDescripcion(descripcion);
        return garantiaRepository.updateGarantia(garantia);
    }

    @Override
    public void deleteGarantiaById(int id) {
        garantiaRepository.deleteGarantiaById(id);
    }

    // HU-21: la garantia sigue vigente si hoy no ha pasado la fecha de vencimiento
    @Override
    public boolean estaVigente(int idGarantia) {
        Garantia garantia = garantiaRepository.selectGarantiaById(idGarantia);
        if (garantia == null) {
            return false;
        }
        return !LocalDate.now().isAfter(garantia.getFechaVencimiento());
    }

}
