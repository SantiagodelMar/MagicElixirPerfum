package service.inputport;

import domain.Inventario;

import java.util.List;

public interface InventarioServiceInterface {

    public Inventario createInventario(int idInventario, int idPerfume, int stockDisponible);
    public Inventario selectInventarioById(int id);
    public Inventario selectInventarioByPerfume(int idPerfume);
    public List<Inventario> selectAllInventarios();
    public Inventario updateStock(int idInventario, int nuevoStock);
    public void deleteInventarioById(int id);

}
