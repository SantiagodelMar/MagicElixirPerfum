package repository;

import domain.Devolucion;
import service.outputport.DevolucionRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class DevolucionRepositoryImp implements DevolucionRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Devolucion> devoluciones = new ArrayList<>();

    @Override
    public Devolucion createDevolucion(Devolucion devolucion) {
        devoluciones.add(devolucion);
        return devolucion;
    }

    @Override
    public Devolucion selectDevolucionById(int id) {
        for (int i = 0; i < devoluciones.size(); i++) {
            if (devoluciones.get(i).getIdDevolucion() == id) {
                return devoluciones.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Devolucion> selectAllDevoluciones() {
        return devoluciones;
    }

    @Override
    public Devolucion updateDevolucion(Devolucion devolucion) {
        for (int i = 0; i < devoluciones.size(); i++) {
            if (devoluciones.get(i).getIdDevolucion() == devolucion.getIdDevolucion()) {
                devoluciones.set(i, devolucion);
                return devolucion;
            }
        }
        return null;
    }

    @Override
    public void deleteDevolucionById(int id) {
        for (int i = 0; i < devoluciones.size(); i++) {
            if (devoluciones.get(i).getIdDevolucion() == id) {
                devoluciones.remove(i);
                return;
            }
        }
    }

}
