package service.outputport;

import domain.Proveedor;

import java.util.List;

public interface ProveedorRepositoryPort {

    public Proveedor createProveedor(Proveedor proveedor);
    public Proveedor selectProveedorById(int id);
    public List<Proveedor> selectAllProveedores();
    public Proveedor updateProveedor(Proveedor proveedor);
    public void deleteProveedorById(int id);

}
