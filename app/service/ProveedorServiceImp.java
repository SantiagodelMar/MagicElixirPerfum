package service;

import domain.Proveedor;
import service.inputport.ProveedorServiceInterface;
import service.outputport.ProveedorRepositoryPort;

import java.util.List;

public class ProveedorServiceImp implements ProveedorServiceInterface {

    private final ProveedorRepositoryPort proveedorRepository;

    public ProveedorServiceImp(ProveedorRepositoryPort proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    // HU-15: registrar proveedores, sin documentos repetidos
    @Override
    public Proveedor createProveedor(int idProveedor, String nombre, String documento, String telefono,
                                     String correo, String direccion) {

        for (Proveedor existente : proveedorRepository.selectAllProveedores()) {
            if (existente.getDocumento().equalsIgnoreCase(documento)) {
                return null;
            }
        }

        Proveedor proveedor = new Proveedor(idProveedor, nombre, documento, telefono, correo, direccion);
        return proveedorRepository.createProveedor(proveedor);
    }

    @Override
    public Proveedor selectProveedorById(int id) {
        return proveedorRepository.selectProveedorById(id);
    }

    @Override
    public List<Proveedor> selectAllProveedores() {
        return proveedorRepository.selectAllProveedores();
    }

    @Override
    public Proveedor updateProveedor(int idProveedor, String telefono, String correo, String direccion) {
        Proveedor proveedor = proveedorRepository.selectProveedorById(idProveedor);
        if (proveedor == null) {
            return null;
        }

        proveedor.setTelefono(telefono);
        proveedor.setCorreo(correo);
        proveedor.setDireccion(direccion);

        return proveedorRepository.updateProveedor(proveedor);
    }

    // Activa o desactiva el proveedor
    @Override
    public Proveedor cambiarEstadoProveedor(int id) {
        Proveedor proveedor = proveedorRepository.selectProveedorById(id);
        if (proveedor == null) {
            return null;
        }

        proveedor.setActivo(!proveedor.isActivo());
        return proveedorRepository.updateProveedor(proveedor);
    }

    @Override
    public void deleteProveedorById(int id) {
        proveedorRepository.deleteProveedorById(id);
    }

}
