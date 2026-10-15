package service.inputport;

import domain.Devolucion;
import domain.enums.MotivoDevolucion;

import java.time.LocalDate;
import java.util.List;

public interface DevolucionServiceInterface {

    public Devolucion createDevolucion(int idDevolucion, int idPedido, LocalDate fechaEntrega,
                                       MotivoDevolucion motivo, String descripcion, double totalPagado);
    public Devolucion selectDevolucionById(int id);
    public List<Devolucion> selectAllDevoluciones();
    public Devolucion updateDevolucion(int idDevolucion, MotivoDevolucion motivo, String descripcion);
    public void deleteDevolucionById(int id);
    public Devolucion registrarReembolso(int id);

}
