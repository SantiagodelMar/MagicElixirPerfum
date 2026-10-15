package service.inputport;

import domain.Perfume;

import java.util.List;

public interface PerfumeServiceInterface {

    public Perfume createPerfume(int idPerfume, String nombre, String marca, double precio, int mililitros,
                                 String notasOlfativas, int idCategoria);
    public Perfume selectPerfumeById(int id);
    public List<Perfume> selectAllPerfumes();
    public List<Perfume> selectPerfumesByCategoria(int idCategoria);
    public Perfume updatePerfume(int idPerfume, String nombre, String marca, int mililitros, String notasOlfativas, int idCategoria);
    public Perfume actualizarPrecio(int idPerfume, double nuevoPrecio);
    public void deletePerfumeById(int id);

}
