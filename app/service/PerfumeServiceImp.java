package service;

import domain.Categoria;
import domain.Perfume;
import service.inputport.PerfumeServiceInterface;
import service.outputport.CategoriaRepositoryPort;
import service.outputport.PerfumeRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class PerfumeServiceImp implements PerfumeServiceInterface {

    private final PerfumeRepositoryPort perfumeRepository;
    private final CategoriaRepositoryPort categoriaRepository;

    public PerfumeServiceImp(PerfumeRepositoryPort perfumeRepository, CategoriaRepositoryPort categoriaRepository) {
        this.perfumeRepository = perfumeRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // HU-04: registrar perfume con su categoria (HU-06)
    @Override
    public Perfume createPerfume(int idPerfume, String nombre, String marca, double precio, int mililitros,
                                 String notasOlfativas, int idCategoria) {

        Categoria categoria = categoriaRepository.selectCategoriaById(idCategoria);
        if (categoria == null) {
            return null;
        }

        Perfume perfume = new Perfume(idPerfume, nombre, marca, precio, mililitros, notasOlfativas, categoria);
        return perfumeRepository.createPerfume(perfume);
    }

    @Override
    public Perfume selectPerfumeById(int id) {
        return perfumeRepository.selectPerfumeById(id);
    }

    @Override
    public List<Perfume> selectAllPerfumes() {
        return perfumeRepository.selectAllPerfumes();
    }

    // HU-06: ver los perfumes de una sola categoria
    @Override
    public List<Perfume> selectPerfumesByCategoria(int idCategoria) {
        List<Perfume> resultado = new ArrayList<>();

        for (Perfume perfume : perfumeRepository.selectAllPerfumes()) {
            if (perfume.getCategoria().getIdCategoria() == idCategoria) {
                resultado.add(perfume);
            }
        }
        return resultado;
    }

    @Override
    public Perfume updatePerfume(int idPerfume, String nombre, String marca, int mililitros, String notasOlfativas, int idCategoria) {
        Perfume perfume = perfumeRepository.selectPerfumeById(idPerfume);
        Categoria categoria = categoriaRepository.selectCategoriaById(idCategoria);
        if (perfume == null || categoria == null) {
            return null;
        }

        perfume.setNombre(nombre);
        perfume.setMarca(marca);
        perfume.setMililitros(mililitros);
        perfume.setNotasOlfativas(notasOlfativas);
        perfume.setCategoria(categoria);

        return perfumeRepository.updatePerfume(perfume);
    }

    // HU-05: actualizar el precio cuando cambia el proveedor
    @Override
    public Perfume actualizarPrecio(int idPerfume, double nuevoPrecio) {
        Perfume perfume = perfumeRepository.selectPerfumeById(idPerfume);
        if (perfume == null || nuevoPrecio <= 0) {
            return null;
        }

        perfume.setPrecio(nuevoPrecio);
        return perfumeRepository.updatePerfume(perfume);
    }

    @Override
    public void deletePerfumeById(int id) {
        perfumeRepository.deletePerfumeById(id);
    }

}
