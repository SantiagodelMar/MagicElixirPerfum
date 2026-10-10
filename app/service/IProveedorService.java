package service;

import domain.Proveedor;

import java.util.List;

public interface IProveedorService {

    Proveedor registrarProveedor(
            int idProveedor,
            String nombre,
            String documento,
            String telefono,
            String correo,
            String direccion
    );

    Proveedor buscarPorId(int id);

    List<Proveedor> obtenerTodos();

}
