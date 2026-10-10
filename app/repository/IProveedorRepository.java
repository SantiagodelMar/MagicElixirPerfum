package repository;

import domain.Proveedor;

import java.util.List;

public interface IProveedorRepository {

    void guardar(Proveedor proveedor);

    Proveedor buscarPorId(int id);

    List<Proveedor> obtenerTodos();

}
