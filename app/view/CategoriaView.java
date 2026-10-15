package view;

import domain.Categoria;
import service.inputport.CategoriaServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class CategoriaView {

    private final CategoriaServiceInterface categoriaService;

    public CategoriaView(CategoriaServiceInterface categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void createCategoria() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la categoria");

        if (categoriaService.selectCategoriaById(id) != null) {
            System.out.println("Ya existe una categoria con ese id");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nombre de la categoria");
        String descripcion = FormTypeValidator.stringValidator("Ingrese la descripcion");

        categoriaService.createCategoria(id, nombre, descripcion);
        System.out.println("Categoria registrada");
    }

    public void selectCategoriaById() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la categoria a buscar");
        Categoria categoria = categoriaService.selectCategoriaById(id);

        if (categoria == null) {
            System.out.println("No se encontro la categoria");
        } else {
            mostrarCategoria(categoria);
        }
    }

    public void selectAllCategorias() {
        List<Categoria> categorias = categoriaService.selectAllCategorias();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorias registradas");
            return;
        }

        for (Categoria categoria : categorias) {
            mostrarCategoria(categoria);
        }
    }

    public void updateCategoria() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la categoria a actualizar");

        if (categoriaService.selectCategoriaById(id) == null) {
            System.out.println("No se encontro la categoria");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nuevo nombre");
        String descripcion = FormTypeValidator.stringValidator("Ingrese la nueva descripcion");

        categoriaService.updateCategoria(id, nombre, descripcion);
        System.out.println("Categoria actualizada");
    }

    public void deleteCategoria() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la categoria a eliminar");

        if (categoriaService.selectCategoriaById(id) == null) {
            System.out.println("No se encontro la categoria");
            return;
        }

        categoriaService.deleteCategoriaById(id);
        System.out.println("Categoria eliminada");
    }

    private void mostrarCategoria(Categoria categoria) {
        System.out.println("Categoria #" + categoria.getIdCategoria()
                + " | " + categoria.getNombre()
                + " | " + categoria.getDescripcion());
    }

}//Fin public class
