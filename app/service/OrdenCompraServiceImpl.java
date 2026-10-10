
package service;

import domain.OrdenCompra;
import domain.Proveedor;
import repository.IOrdenCompraRepository;
import repository.IProveedorRepository;

import java.time.LocalDate;
import java.util.List;

public class OrdenCompraServiceImpl implements IOrdenCompraService {

    private final IOrdenCompraRepository ordenRepository;
    private final IProveedorRepository proveedorRepository;

    // Constructor: recibe las dependencias necesarias.
    public OrdenCompraServiceImpl(
            IOrdenCompraRepository ordenRepository,
            IProveedorRepository proveedorRepository) {

        if (ordenRepository == null || proveedorRepository == null) {
            throw new IllegalArgumentException(
                    "Los repositorios no pueden ser nulos."
            );
        }

        this.ordenRepository = ordenRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public OrdenCompra registrarOrdenCompra(
            int idOrdenCompra,
            int idProveedor,
            double total,
            String observaciones) {

        if (idOrdenCompra <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la orden debe ser mayor que cero."
            );
        }

        if (idProveedor <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del proveedor debe ser mayor que cero."
            );
        }

        if (total <= 0 || Double.isNaN(total) || Double.isInfinite(total)) {
            throw new IllegalArgumentException(
                    "El total debe ser un valor válido mayor que cero."
            );
        }

        if (ordenRepository.buscarPorId(idOrdenCompra) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una orden de compra con ese identificador."
            );
        }

        Proveedor proveedor = proveedorRepository.buscarPorId(idProveedor);

        if (proveedor == null) {
            throw new IllegalArgumentException(
                    "No existe un proveedor con ese identificador."
            );
        }

        if (!proveedor.isActivo()) {
            throw new IllegalArgumentException(
                    "No se puede crear una orden para un proveedor inactivo."
            );
        }

        OrdenCompra orden = new OrdenCompra(
                idOrdenCompra,
                proveedor,
                LocalDate.now(),
                total,
                observaciones
        );

        ordenRepository.guardar(orden);

        return orden;
    }

    @Override
    public OrdenCompra buscarPorId(int id) {
        return ordenRepository.buscarPorId(id);
    }

    @Override
    public List<OrdenCompra> obtenerTodos() {
        return ordenRepository.obtenerTodos();
    }
}
