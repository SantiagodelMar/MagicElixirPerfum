package repository;

import domain.Proveedor;
import service.outputport.ProveedorRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class ProveedorRepositoryImp implements ProveedorRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Proveedor> proveedores = new ArrayList<>();

    @Override
    public Proveedor createProveedor(Proveedor proveedor) {
        proveedores.add(proveedor);
        return proveedor;
    }

    @Override
    public Proveedor selectProveedorById(int id) {
        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getIdProveedor() == id) {
                return proveedores.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Proveedor> selectAllProveedores() {
        return proveedores;
    }

    @Override
    public Proveedor updateProveedor(Proveedor proveedor) {
        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getIdProveedor() == proveedor.getIdProveedor()) {
                proveedores.set(i, proveedor);
                return proveedor;
            }
        }
        return null;
    }

    @Override
    public void deleteProveedorById(int id) {
        for (int i = 0; i < proveedores.size(); i++) {
            if (proveedores.get(i).getIdProveedor() == id) {
                proveedores.remove(i);
                return;
            }
        }
    }

}
