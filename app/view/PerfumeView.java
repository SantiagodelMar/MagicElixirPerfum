package view;

import domain.Perfume;
import service.inputport.PerfumeServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class PerfumeView {

    private final PerfumeServiceInterface perfumeService;

    public PerfumeView(PerfumeServiceInterface perfumeService) {
        this.perfumeService = perfumeService;
    }

    public void createPerfume() {
        int id = FormTypeValidator.intValidator("Ingrese el id del perfume");

        if (perfumeService.selectPerfumeById(id) != null) {
            System.out.println("Ya existe un perfume con ese id");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nombre del perfume");
        String marca = FormTypeValidator.stringValidator("Ingrese la marca");
        double precio = FormTypeValidator.doubleValidator("Ingrese el precio");
        int mililitros = FormTypeValidator.intValidator("Ingrese los mililitros");
        String notas = FormTypeValidator.stringValidator("Ingrese las notas olfativas");
        int idCategoria = FormTypeValidator.intValidator("Ingrese el id de la categoria (1. Arabe Masculina, 2. Masculina, 3. Arabe Femenina, 4. Femenina)");

        Perfume perfume = perfumeService.createPerfume(id, nombre, marca, precio, mililitros, notas, idCategoria);

        if (perfume == null) {
            System.out.println("No existe esa categoria, el perfume no se registro");
        } else {
            System.out.println("Perfume registrado. Recuerde crearle el inventario");
        }
    }

    public void selectPerfumeById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del perfume a buscar");
        Perfume perfume = perfumeService.selectPerfumeById(id);

        if (perfume == null) {
            System.out.println("No se encontro el perfume");
        } else {
            mostrarPerfume(perfume);
        }
    }

    public void selectAllPerfumes() {
        List<Perfume> perfumes = perfumeService.selectAllPerfumes();

        if (perfumes.isEmpty()) {
            System.out.println("No hay perfumes registrados");
            return;
        }

        for (Perfume perfume : perfumes) {
            mostrarPerfume(perfume);
        }
        System.out.println("Total de perfumes: " + perfumes.size());
    }

    // HU-06
    public void selectPerfumesByCategoria() {
        int idCategoria = FormTypeValidator.intValidator("Ingrese el id de la categoria (1. Arabe Masculina, 2. Masculina, 3. Arabe Femenina, 4. Femenina)");
        List<Perfume> perfumes = perfumeService.selectPerfumesByCategoria(idCategoria);

        if (perfumes.isEmpty()) {
            System.out.println("No hay perfumes en esa categoria");
            return;
        }

        for (Perfume perfume : perfumes) {
            mostrarPerfume(perfume);
        }
        System.out.println("Total en la categoria: " + perfumes.size());
    }

    public void updatePerfume() {
        int id = FormTypeValidator.intValidator("Ingrese el id del perfume a actualizar");

        if (perfumeService.selectPerfumeById(id) == null) {
            System.out.println("No se encontro el perfume");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nuevo nombre");
        String marca = FormTypeValidator.stringValidator("Ingrese la nueva marca");
        int mililitros = FormTypeValidator.intValidator("Ingrese los mililitros");
        String notas = FormTypeValidator.stringValidator("Ingrese las notas olfativas");
        int idCategoria = FormTypeValidator.intValidator("Ingrese el id de la categoria");

        if (perfumeService.updatePerfume(id, nombre, marca, mililitros, notas, idCategoria) == null) {
            System.out.println("No existe esa categoria, no se actualizo");
        } else {
            System.out.println("Perfume actualizado");
        }
    }

    // HU-05
    public void actualizarPrecio() {
        int id = FormTypeValidator.intValidator("Ingrese el id del perfume");
        double nuevoPrecio = FormTypeValidator.doubleValidator("Ingrese el nuevo precio");

        Perfume perfume = perfumeService.actualizarPrecio(id, nuevoPrecio);

        if (perfume == null) {
            System.out.println("No se encontro el perfume o el precio no es valido");
        } else {
            System.out.println("Nuevo precio de " + perfume.getNombre() + ": $" + perfume.getPrecio());
        }
    }

    public void deletePerfume() {
        int id = FormTypeValidator.intValidator("Ingrese el id del perfume a eliminar");

        if (perfumeService.selectPerfumeById(id) == null) {
            System.out.println("No se encontro el perfume");
            return;
        }

        perfumeService.deletePerfumeById(id);
        System.out.println("Perfume eliminado");
    }

    private void mostrarPerfume(Perfume perfume) {
        System.out.println("Perfume #" + perfume.getIdPerfume()
                + " | " + perfume.getNombre() + " - " + perfume.getMarca()
                + " (" + perfume.getMililitros() + " ml)"
                + " | " + perfume.getCategoria().getNombre()
                + " | Notas: " + perfume.getNotasOlfativas()
                + " | $" + perfume.getPrecio());
    }

}//Fin public class
