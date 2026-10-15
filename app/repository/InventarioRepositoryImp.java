package repository;

import domain.Inventario;
import service.outputport.InventarioRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class InventarioRepositoryImp implements InventarioRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Inventario> inventarios = new ArrayList<>();

    @Override
    public Inventario createInventario(Inventario inventario) {
        inventarios.add(inventario);
        return inventario;
    }

    @Override
    public Inventario selectInventarioById(int id) {
        for (int i = 0; i < inventarios.size(); i++) {
            if (inventarios.get(i).getIdInventario() == id) {
                return inventarios.get(i);
            }
        }
        return null;
    }

    @Override
    public Inventario selectInventarioByPerfume(int idPerfume) {
        for (int i = 0; i < inventarios.size(); i++) {
            if (inventarios.get(i).getPerfume().getIdPerfume() == idPerfume) {
                return inventarios.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Inventario> selectAllInventarios() {
        return inventarios;
    }

    @Override
    public Inventario updateInventario(Inventario inventario) {
        for (int i = 0; i < inventarios.size(); i++) {
            if (inventarios.get(i).getIdInventario() == inventario.getIdInventario()) {
                inventarios.set(i, inventario);
                return inventario;
            }
        }
        return null;
    }

    @Override
    public void deleteInventarioById(int id) {
        for (int i = 0; i < inventarios.size(); i++) {
            if (inventarios.get(i).getIdInventario() == id) {
                inventarios.remove(i);
                return;
            }
        }
    }

}
