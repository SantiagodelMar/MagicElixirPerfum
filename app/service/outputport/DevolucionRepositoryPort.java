package service.outputport;

import domain.Devolucion;

import java.util.List;

public interface DevolucionRepositoryPort {

    public Devolucion createDevolucion(Devolucion devolucion);
    public Devolucion selectDevolucionById(int id);
    public List<Devolucion> selectAllDevoluciones();
    public Devolucion updateDevolucion(Devolucion devolucion);
    public void deleteDevolucionById(int id);

}
