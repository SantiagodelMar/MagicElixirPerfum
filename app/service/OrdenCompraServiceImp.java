package service;

import domain.OrdenCompra;
import domain.Proveedor;
import domain.enums.EstadoOrdenCompra;
import service.inputport.OrdenCompraServiceInterface;
import service.outputport.OrdenCompraRepositoryPort;
import service.outputport.ProveedorRepositoryPort;

import java.time.LocalDate;
import java.util.List;

public class OrdenCompraServiceImp implements OrdenCompraServiceInterface {

    private final OrdenCompraRepositoryPort ordenCompraRepository;
    private final ProveedorRepositoryPort proveedorRepository;

    public OrdenCompraServiceImp(OrdenCompraRepositoryPort ordenCompraRepository, ProveedorRepositoryPort proveedorRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.proveedorRepository = proveedorRepository;
    }

    // HU-16: crear orden de compra a un proveedor activo
    @Override
    public OrdenCompra createOrdenCompra(int idOrdenCompra, int idProveedor, double total, String observaciones) {
        Proveedor proveedor = proveedorRepository.selectProveedorById(idProveedor);
        if (proveedor == null || !proveedor.isActivo() || total <= 0) {
            return null;
        }

        OrdenCompra ordenCompra = new OrdenCompra(idOrdenCompra, proveedor, LocalDate.now(),
                EstadoOrdenCompra.PENDIENTE, total, observaciones);

        return ordenCompraRepository.createOrdenCompra(ordenCompra);
    }

    @Override
    public OrdenCompra selectOrdenCompraById(int id) {
        return ordenCompraRepository.selectOrdenCompraById(id);
    }

    @Override
    public List<OrdenCompra> selectAllOrdenesCompra() {
        return ordenCompraRepository.selectAllOrdenesCompra();
    }

    @Override
    public OrdenCompra updateOrdenCompra(int idOrdenCompra, double total, String observaciones) {
        OrdenCompra ordenCompra = ordenCompraRepository.selectOrdenCompraById(idOrdenCompra);
        if (ordenCompra == null || total <= 0) {
            return null;
        }

        ordenCompra.setTotal(total);
        ordenCompra.setObservaciones(observaciones);

        return ordenCompraRepository.updateOrdenCompra(ordenCompra);
    }

    @Override
    public OrdenCompra cambiarEstadoOrdenCompra(int idOrdenCompra, EstadoOrdenCompra nuevoEstado) {
        OrdenCompra ordenCompra = ordenCompraRepository.selectOrdenCompraById(idOrdenCompra);
        if (ordenCompra == null) {
            return null;
        }

        ordenCompra.setEstado(nuevoEstado);
        return ordenCompraRepository.updateOrdenCompra(ordenCompra);
    }

    @Override
    public void deleteOrdenCompraById(int id) {
        ordenCompraRepository.deleteOrdenCompraById(id);
    }

}
