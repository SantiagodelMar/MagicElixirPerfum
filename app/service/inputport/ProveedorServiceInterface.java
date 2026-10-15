package service.inputport;

import domain.Proveedor;

import java.util.List;

public interface ProveedorServiceInterface {

    public Proveedor createProveedor(int idProveedor, String nombre, String documento, String telefono,
                                     String correo, String direccion);
    public Proveedor selectProveedorById(int id);
    public List<Proveedor> selectAllProveedores();
    public Proveedor updateProveedor(int idProveedor, String telefono, String correo, String direccion);
    public Proveedor cambiarEstadoProveedor(int id);
    public void deleteProveedorById(int id);

}
