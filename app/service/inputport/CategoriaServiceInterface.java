package service.inputport;

import domain.Categoria;

import java.util.List;

public interface CategoriaServiceInterface {

    public Categoria createCategoria(int idCategoria, String nombre, String descripcion);
    public Categoria selectCategoriaById(int id);
    public List<Categoria> selectAllCategorias();
    public Categoria updateCategoria(int idCategoria, String nombre, String descripcion);
    public void deleteCategoriaById(int id);

}
