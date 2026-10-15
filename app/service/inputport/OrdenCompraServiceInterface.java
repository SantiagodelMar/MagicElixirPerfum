package service.inputport;

import domain.OrdenCompra;
import domain.enums.EstadoOrdenCompra;

import java.util.List;

public interface OrdenCompraServiceInterface {

    public OrdenCompra createOrdenCompra(int idOrdenCompra, int idProveedor, double total, String observaciones);
    public OrdenCompra selectOrdenCompraById(int id);
    public List<OrdenCompra> selectAllOrdenesCompra();
    public OrdenCompra updateOrdenCompra(int idOrdenCompra, double total, String observaciones);
    public OrdenCompra cambiarEstadoOrdenCompra(int idOrdenCompra, EstadoOrdenCompra nuevoEstado);
    public void deleteOrdenCompraById(int id);

}
