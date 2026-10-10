
package repository;

import domain.OrdenCompra;
import java.util.List;

public interface IOrdenCompraRepository {

    // Guarda una orden de compra.
    void guardar(OrdenCompra ordenCompra);

    // Busca una orden por su identificador.
    OrdenCompra buscarPorId(int id);

    // Obtiene todas las órdenes registradas.
    List<OrdenCompra> obtenerTodos();
}
