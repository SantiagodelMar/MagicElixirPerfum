package service;

import domain.Categoria;
import service.inputport.CategoriaServiceInterface;
import service.outputport.CategoriaRepositoryPort;

import java.util.List;

public class CategoriaServiceImp implements CategoriaServiceInterface {

    private final CategoriaRepositoryPort categoriaRepository;

    public CategoriaServiceImp(CategoriaRepositoryPort categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria createCategoria(int idCategoria, String nombre, String descripcion) {
        Categoria categoria = new Categoria(idCategoria, nombre, descripcion);
        return categoriaRepository.createCategoria(categoria);
    }

    @Override
    public Categoria selectCategoriaById(int id) {
        return categoriaRepository.selectCategoriaById(id);
    }

    @Override
    public List<Categoria> selectAllCategorias() {
        return categoriaRepository.selectAllCategorias();
    }

    @Override
    public Categoria updateCategoria(int idCategoria, String nombre, String descripcion) {
        Categoria categoria = categoriaRepository.selectCategoriaById(idCategoria);
        if (categoria == null) {
            return null;
        }

        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);

        return categoriaRepository.updateCategoria(categoria);
    }

    @Override
    public void deleteCategoriaById(int id) {
        categoriaRepository.deleteCategoriaById(id);
    }

}
