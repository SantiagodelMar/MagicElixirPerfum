package service.outputport;

import domain.Categoria;

import java.util.List;

public interface CategoriaRepositoryPort {

    public Categoria createCategoria(Categoria categoria);
    public Categoria selectCategoriaById(int id);
    public List<Categoria> selectAllCategorias();
    public Categoria updateCategoria(Categoria categoria);
    public void deleteCategoriaById(int id);

}
