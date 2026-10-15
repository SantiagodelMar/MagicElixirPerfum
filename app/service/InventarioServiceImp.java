package service;

import domain.Inventario;
import domain.Perfume;
import service.helpers.SetInventarioHelper;
import service.inputport.InventarioServiceInterface;
import service.outputport.InventarioRepositoryPort;
import service.outputport.PerfumeRepositoryPort;

import java.util.List;

public class InventarioServiceImp implements InventarioServiceInterface {

    private final InventarioRepositoryPort inventarioRepository;
    private final PerfumeRepositoryPort perfumeRepository;

    public InventarioServiceImp(InventarioRepositoryPort inventarioRepository, PerfumeRepositoryPort perfumeRepository) {
        this.inventarioRepository = inventarioRepository;
        this.perfumeRepository = perfumeRepository;
    }

    @Override
    public Inventario createInventario(int idInventario, int idPerfume, int stockDisponible) {
        Perfume perfume = perfumeRepository.selectPerfumeById(idPerfume);
        if (perfume == null || stockDisponible < 0) {
            return null;
        }

        Inventario inventario = new Inventario(idInventario, perfume, stockDisponible,
                SetInventarioHelper.calcularEstado(stockDisponible));

        return inventarioRepository.createInventario(inventario);
    }

    @Override
    public Inventario selectInventarioById(int id) {
        return inventarioRepository.selectInventarioById(id);
    }

    // HU-07: ver cuantas unidades hay de un perfume
    @Override
    public Inventario selectInventarioByPerfume(int idPerfume) {
        return inventarioRepository.selectInventarioByPerfume(idPerfume);
    }

    @Override
    public List<Inventario> selectAllInventarios() {
        return inventarioRepository.selectAllInventarios();
    }

    @Override
    public Inventario updateStock(int idInventario, int nuevoStock) {
        Inventario inventario = inventarioRepository.selectInventarioById(idInventario);
        if (inventario == null || nuevoStock < 0) {
            return null;
        }

        inventario.setStockDisponible(nuevoStock);
        inventario.setEstadoDisponibilidad(SetInventarioHelper.calcularEstado(nuevoStock));

        return inventarioRepository.updateInventario(inventario);
    }

    @Override
    public void deleteInventarioById(int id) {
        inventarioRepository.deleteInventarioById(id);
    }

}
