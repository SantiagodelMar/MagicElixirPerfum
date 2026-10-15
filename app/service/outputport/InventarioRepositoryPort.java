package service.outputport;

import domain.Inventario;

import java.util.List;

public interface InventarioRepositoryPort {

    public Inventario createInventario(Inventario inventario);
    public Inventario selectInventarioById(int id);
    public Inventario selectInventarioByPerfume(int idPerfume);
    public List<Inventario> selectAllInventarios();
    public Inventario updateInventario(Inventario inventario);
    public void deleteInventarioById(int id);

}
