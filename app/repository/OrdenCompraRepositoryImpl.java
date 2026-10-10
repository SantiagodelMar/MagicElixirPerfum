
package repository;

import domain.OrdenCompra;
import java.util.ArrayList;
import java.util.List;

public class OrdenCompraRepositoryImpl implements IOrdenCompraRepository {

    // Simulación de la base de datos en memoria.
    private final List<OrdenCompra> baseDeDatos = new ArrayList<>();

    @Override
    public void guardar(OrdenCompra ordenCompra) {

        if (ordenCompra == null) {
            throw new IllegalArgumentException(
                    "La orden de compra no puede ser nula."
            );
        }

        if (buscarPorId(ordenCompra.getIdOrdenCompra()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una orden de compra con ese identificador."
            );
        }

        baseDeDatos.add(ordenCompra);
    }

    @Override
    public OrdenCompra buscarPorId(int id) {

        for (OrdenCompra orden : baseDeDatos) {
            if (orden.getIdOrdenCompra() == id) {
                return orden;
            }
        }

        return null;
    }

    @Override
    public List<OrdenCompra> obtenerTodos() {
        return new ArrayList<>(baseDeDatos);
    }
}
