
package service;

import domain.OrdenCompra;
import java.util.List;

public interface IOrdenCompraService {

    // Registra una nueva orden de compra.
    OrdenCompra registrarOrdenCompra(
            int idOrdenCompra,
            int idProveedor,
            double total,
            String observaciones
    );

    // Busca una orden por su identificador.
    OrdenCompra buscarPorId(int id);

    // Consulta todas las órdenes registradas.
    List<OrdenCompra> obtenerTodos();
}
