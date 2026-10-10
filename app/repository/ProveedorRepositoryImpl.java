
package repository;

import domain.Proveedor;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra los proveedores mediante una colección en memoria.
 * Esta implementación permite probar el módulo sin una base de datos real.
 */
public class ProveedorRepositoryImpl implements IProveedorRepository {

    // Simulación de la base de datos en memoria.
    private final List<Proveedor> baseDeDatos = new ArrayList<>();

    @Override
    public void guardar(Proveedor proveedor) {
        if (proveedor == null) {
            throw new IllegalArgumentException(
                    "El proveedor no puede ser nulo."
            );
        }

        if (buscarPorId(proveedor.getIdProveedor()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un proveedor con ese identificador."
            );
        }

        baseDeDatos.add(proveedor);
    }

    @Override
    public Proveedor buscarPorId(int id) {
        for (Proveedor proveedor : baseDeDatos) {
            if (proveedor.getIdProveedor() == id) {
                return proveedor;
            }
        }

        return null;
    }

    @Override
    public List<Proveedor> obtenerTodos() {
        return new ArrayList<>(baseDeDatos);
    }
}
