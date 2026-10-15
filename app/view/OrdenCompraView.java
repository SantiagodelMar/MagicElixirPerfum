package view;

import domain.OrdenCompra;
import domain.enums.EstadoOrdenCompra;
import service.helpers.SetOrdenCompraHelper;
import service.inputport.OrdenCompraServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class OrdenCompraView {

    private final OrdenCompraServiceInterface ordenCompraService;

    public OrdenCompraView(OrdenCompraServiceInterface ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    public void createOrdenCompra() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la orden de compra");

        if (ordenCompraService.selectOrdenCompraById(id) != null) {
            System.out.println("Ya existe una orden con ese id");
            return;
        }

        int idProveedor = FormTypeValidator.intValidator("Ingrese el id del proveedor");
        double total = FormTypeValidator.doubleValidator("Ingrese el total de la orden");
        String observaciones = FormTypeValidator.stringValidator("Ingrese las observaciones");

        OrdenCompra orden = ordenCompraService.createOrdenCompra(id, idProveedor, total, observaciones);

        if (orden == null) {
            System.out.println("No se pudo crear: el proveedor no existe, esta inactivo o el total no es valido");
        } else {
            System.out.println("Orden de compra creada para " + orden.getProveedor().getNombre());
        }
    }

    public void selectOrdenCompraById() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la orden a buscar");
        OrdenCompra orden = ordenCompraService.selectOrdenCompraById(id);

        if (orden == null) {
            System.out.println("No se encontro la orden");
        } else {
            mostrarOrden(orden);
        }
    }

    public void selectAllOrdenesCompra() {
        List<OrdenCompra> ordenes = ordenCompraService.selectAllOrdenesCompra();

        if (ordenes.isEmpty()) {
            System.out.println("No hay ordenes de compra registradas");
            return;
        }

        for (OrdenCompra orden : ordenes) {
            mostrarOrden(orden);
        }
    }

    public void updateOrdenCompra() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la orden a actualizar");

        if (ordenCompraService.selectOrdenCompraById(id) == null) {
            System.out.println("No se encontro la orden");
            return;
        }

        double total = FormTypeValidator.doubleValidator("Ingrese el nuevo total");
        String observaciones = FormTypeValidator.stringValidator("Ingrese las nuevas observaciones");

        if (ordenCompraService.updateOrdenCompra(id, total, observaciones) == null) {
            System.out.println("El total no es valido");
        } else {
            System.out.println("Orden actualizada");
        }
    }

    public void cambiarEstadoOrdenCompra() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la orden");

        if (ordenCompraService.selectOrdenCompraById(id) == null) {
            System.out.println("No se encontro la orden");
            return;
        }

        EstadoOrdenCompra estado = SetOrdenCompraHelper.getEstadoOrdenCompra();
        OrdenCompra orden = ordenCompraService.cambiarEstadoOrdenCompra(id, estado);
        System.out.println("La orden quedo en estado: " + orden.getEstado().getDescripcion());
    }

    public void deleteOrdenCompra() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la orden a eliminar");

        if (ordenCompraService.selectOrdenCompraById(id) == null) {
            System.out.println("No se encontro la orden");
            return;
        }

        ordenCompraService.deleteOrdenCompraById(id);
        System.out.println("Orden eliminada");
    }

    private void mostrarOrden(OrdenCompra orden) {
        System.out.println("Orden #" + orden.getIdOrdenCompra()
                + " | Proveedor: " + orden.getProveedor().getNombre()
                + " | Fecha: " + orden.getFechaOrden()
                + " | Estado: " + orden.getEstado().getDescripcion()
                + " | Total: $" + orden.getTotal()
                + " | Obs: " + orden.getObservaciones());
    }

}//Fin public class
