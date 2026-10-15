package service.outputport;

import domain.OrdenCompra;

import java.util.List;

public interface OrdenCompraRepositoryPort {

    public OrdenCompra createOrdenCompra(OrdenCompra ordenCompra);
    public OrdenCompra selectOrdenCompraById(int id);
    public List<OrdenCompra> selectAllOrdenesCompra();
    public OrdenCompra updateOrdenCompra(OrdenCompra ordenCompra);
    public void deleteOrdenCompraById(int id);

}
