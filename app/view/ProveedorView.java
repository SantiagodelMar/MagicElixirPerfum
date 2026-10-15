package view;

import domain.Proveedor;
import service.inputport.ProveedorServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class ProveedorView {

    private final ProveedorServiceInterface proveedorService;

    public ProveedorView(ProveedorServiceInterface proveedorService) {
        this.proveedorService = proveedorService;
    }

    public void createProveedor() {
        int id = FormTypeValidator.intValidator("Ingrese el id del proveedor");

        if (proveedorService.selectProveedorById(id) != null) {
            System.out.println("Ya existe un proveedor con ese id");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nombre del proveedor");
        String documento = FormTypeValidator.stringValidator("Ingrese el NIT o documento");
        String telefono = FormTypeValidator.stringValidator("Ingrese el telefono");
        String correo = FormTypeValidator.stringValidator("Ingrese el correo");
        String direccion = FormTypeValidator.stringValidator("Ingrese la direccion");

        Proveedor proveedor = proveedorService.createProveedor(id, nombre, documento, telefono, correo, direccion);

        if (proveedor == null) {
            System.out.println("Ya existe un proveedor con ese documento");
        } else {
            System.out.println("Proveedor registrado");
        }
    }

    public void selectProveedorById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del proveedor a buscar");
        Proveedor proveedor = proveedorService.selectProveedorById(id);

        if (proveedor == null) {
            System.out.println("No se encontro el proveedor");
        } else {
            mostrarProveedor(proveedor);
        }
    }

    public void selectAllProveedores() {
        List<Proveedor> proveedores = proveedorService.selectAllProveedores();

        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados");
            return;
        }

        for (Proveedor proveedor : proveedores) {
            mostrarProveedor(proveedor);
        }
    }

    public void updateProveedor() {
        int id = FormTypeValidator.intValidator("Ingrese el id del proveedor a actualizar");

        if (proveedorService.selectProveedorById(id) == null) {
            System.out.println("No se encontro el proveedor");
            return;
        }

        String telefono = FormTypeValidator.stringValidator("Ingrese el nuevo telefono");
        String correo = FormTypeValidator.stringValidator("Ingrese el nuevo correo");
        String direccion = FormTypeValidator.stringValidator("Ingrese la nueva direccion");

        proveedorService.updateProveedor(id, telefono, correo, direccion);
        System.out.println("Proveedor actualizado");
    }

    public void cambiarEstadoProveedor() {
        int id = FormTypeValidator.intValidator("Ingrese el id del proveedor a activar/desactivar");
        Proveedor proveedor = proveedorService.cambiarEstadoProveedor(id);

        if (proveedor == null) {
            System.out.println("No se encontro el proveedor");
        } else if (proveedor.isActivo()) {
            System.out.println("El proveedor " + proveedor.getNombre() + " quedo activo");
        } else {
            System.out.println("El proveedor " + proveedor.getNombre() + " quedo inactivo");
        }
    }

    public void deleteProveedor() {
        int id = FormTypeValidator.intValidator("Ingrese el id del proveedor a eliminar");

        if (proveedorService.selectProveedorById(id) == null) {
            System.out.println("No se encontro el proveedor");
            return;
        }

        proveedorService.deleteProveedorById(id);
        System.out.println("Proveedor eliminado");
    }

    private void mostrarProveedor(Proveedor proveedor) {
        System.out.println("Proveedor #" + proveedor.getIdProveedor()
                + " | " + proveedor.getNombre()
                + " | Doc: " + proveedor.getDocumento()
                + " | Tel: " + proveedor.getTelefono()
                + " | Correo: " + proveedor.getCorreo()
                + " | Activo: " + (proveedor.isActivo() ? "Si" : "No"));
    }

}//Fin public class
