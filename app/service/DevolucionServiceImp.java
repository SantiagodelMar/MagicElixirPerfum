package service;

import domain.Devolucion;
import domain.enums.EstadoDevolucion;
import domain.enums.MotivoDevolucion;
import service.inputport.DevolucionServiceInterface;
import service.outputport.DevolucionRepositoryPort;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DevolucionServiceImp implements DevolucionServiceInterface {

    // HU-17: reglas de la devolucion
    private static final int DIAS_MAXIMOS = 30;
    private static final double PORCENTAJE_ARREPENTIMIENTO = 0.80;

    private final DevolucionRepositoryPort devolucionRepository;

    public DevolucionServiceImp(DevolucionRepositoryPort devolucionRepository) {
        this.devolucionRepository = devolucionRepository;
    }

    @Override
    public Devolucion createDevolucion(int idDevolucion, int idPedido, LocalDate fechaEntrega,
                                       MotivoDevolucion motivo, String descripcion, double totalPagado) {

        Devolucion devolucion = new Devolucion(idDevolucion, idPedido, fechaEntrega, LocalDate.now(),
                motivo, descripcion, totalPagado);

        // Si esta dentro del plazo se aprueba, si no se rechaza
        long dias = ChronoUnit.DAYS.between(fechaEntrega, devolucion.getFechaSolicitud());

        if (dias >= 0 && dias <= DIAS_MAXIMOS) {
            devolucion.setEstado(EstadoDevolucion.APROBADA);
            devolucion.setMontoReembolso(calcularReembolso(motivo, totalPagado));
        } else {
            devolucion.setEstado(EstadoDevolucion.RECHAZADA);
            devolucion.setMontoReembolso(0);
        }

        return devolucionRepository.createDevolucion(devolucion);
    }

    @Override
    public Devolucion selectDevolucionById(int id) {
        return devolucionRepository.selectDevolucionById(id);
    }

    @Override
    public List<Devolucion> selectAllDevoluciones() {
        return devolucionRepository.selectAllDevoluciones();
    }

    @Override
    public Devolucion updateDevolucion(int idDevolucion, MotivoDevolucion motivo, String descripcion) {
        Devolucion devolucion = devolucionRepository.selectDevolucionById(idDevolucion);
        if (devolucion == null) {
            return null;
        }

        devolucion.setMotivo(motivo);
        devolucion.setDescripcion(descripcion);

        // Si cambia el motivo hay que recalcular lo que se devuelve
        if (devolucion.getEstado() == EstadoDevolucion.APROBADA) {
            devolucion.setMontoReembolso(calcularReembolso(motivo, devolucion.getTotalPagado()));
        }

        return devolucionRepository.updateDevolucion(devolucion);
    }

    @Override
    public void deleteDevolucionById(int id) {
        devolucionRepository.deleteDevolucionById(id);
    }

    @Override
    public Devolucion registrarReembolso(int id) {
        Devolucion devolucion = devolucionRepository.selectDevolucionById(id);
        if (devolucion == null || devolucion.getEstado() != EstadoDevolucion.APROBADA) {
            return null;
        }

        devolucion.setEstado(EstadoDevolucion.REEMBOLSADA);
        return devolucionRepository.updateDevolucion(devolucion);
    }

    // Por arrepentimiento solo se devuelve el 80%, en los demas casos todo
    private double calcularReembolso(MotivoDevolucion motivo, double totalPagado) {
        if (motivo == MotivoDevolucion.ARREPENTIMIENTO_DE_COMPRA) {
            return totalPagado * PORCENTAJE_ARREPENTIMIENTO;
        }
        return totalPagado;
    }

}
