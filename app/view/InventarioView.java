package view;

import domain.Inventario;
import service.inputport.InventarioServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class InventarioView {

    private final InventarioServiceInterface inventarioService;

    public InventarioView(InventarioServiceInterface inventarioService) {
        this.inventarioService = inventarioService;
    }

    public void createInventario() {
        int id = FormTypeValidator.intValidator("Ingrese el id del inventario");

        if (inventarioService.selectInventarioById(id) != null) {
            System.out.println("Ya existe un inventario con ese id");
            return;
        }

        int idPerfume = FormTypeValidator.intValidator("Ingrese el id del perfume");

        if (inventarioService.selectInventarioByPerfume(idPerfume) != null) {
            System.out.println("Ese perfume ya tiene inventario, use actualizar stock");
            return;
        }

        int stock = FormTypeValidator.intValidator("Ingrese la cantidad disponible");

        Inventario inventario = inventarioService.createInventario(id, idPerfume, stock);

        if (inventario == null) {
            System.out.println("No existe el perfume o la cantidad no es valida");
        } else {
            System.out.println("Inventario registrado: " + inventario.getEstadoDisponibilidad().getDescripcion());
        }
    }

    public void selectInventarioById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del inventario a buscar");
        Inventario inventario = inventarioService.selectInventarioById(id);

        if (inventario == null) {
            System.out.println("No se encontro el inventario");
        } else {
            mostrarInventario(inventario);
        }
    }

    // HU-07 y HU-08: consultar si hay stock de un perfume
    public void consultarStockPerfume() {
        int idPerfume = FormTypeValidator.intValidator("Ingrese el id del perfume");
        Inventario inventario = inventarioService.selectInventarioByPerfume(idPerfume);

        if (inventario == null) {
            System.out.println("Ese perfume no tiene inventario registrado");
        } else {
            mostrarInventario(inventario);
            if (inventario.getStockDisponible() == 0) {
                System.out.println("¡Atencion! Este perfume esta agotado, no lo ofrezca");
            }
        }
    }

    public void selectAllInventarios() {
        List<Inventario> inventarios = inventarioService.selectAllInventarios();

        if (inventarios.isEmpty()) {
            System.out.println("No hay inventario registrado");
            return;
        }

        for (Inventario inventario : inventarios) {
            mostrarInventario(inventario);
        }
    }

    public void updateStock() {
        int id = FormTypeValidator.intValidator("Ingrese el id del inventario");

        if (inventarioService.selectInventarioById(id) == null) {
            System.out.println("No se encontro el inventario");
            return;
        }

        int nuevoStock = FormTypeValidator.intValidator("Ingrese la nueva cantidad disponible");
        Inventario inventario = inventarioService.updateStock(id, nuevoStock);

        if (inventario == null) {
            System.out.println("La cantidad no puede ser negativa");
        } else {
            System.out.println("Stock actualizado: " + inventario.getStockDisponible()
                    + " (" + inventario.getEstadoDisponibilidad().getDescripcion() + ")");
        }
    }

    public void deleteInventario() {
        int id = FormTypeValidator.intValidator("Ingrese el id del inventario a eliminar");

        if (inventarioService.selectInventarioById(id) == null) {
            System.out.println("No se encontro el inventario");
            return;
        }

        inventarioService.deleteInventarioById(id);
        System.out.println("Inventario eliminado");
    }

    private void mostrarInventario(Inventario inventario) {
        System.out.println("Inventario #" + inventario.getIdInventario()
                + " | " + inventario.getPerfume().getNombre()
                + " | Stock: " + inventario.getStockDisponible()
                + " | " + inventario.getEstadoDisponibilidad().getDescripcion());
    }

}//Fin public class
