package repository;

import domain.Categoria;
import service.outputport.CategoriaRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositoryImp implements CategoriaRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Categoria> categorias = new ArrayList<>();

    @Override
    public Categoria createCategoria(Categoria categoria) {
        categorias.add(categoria);
        return categoria;
    }

    @Override
    public Categoria selectCategoriaById(int id) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getIdCategoria() == id) {
                return categorias.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Categoria> selectAllCategorias() {
        return categorias;
    }

    @Override
    public Categoria updateCategoria(Categoria categoria) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getIdCategoria() == categoria.getIdCategoria()) {
                categorias.set(i, categoria);
                return categoria;
            }
        }
        return null;
    }

    @Override
    public void deleteCategoriaById(int id) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getIdCategoria() == id) {
                categorias.remove(i);
                return;
            }
        }
    }

}
