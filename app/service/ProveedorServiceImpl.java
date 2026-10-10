package service;

import domain.Proveedor;
import repository.IProveedorRepository;

import java.util.List;

public class ProveedorServiceImpl implements IProveedorService {

    private final IProveedorRepository repository;

    public ProveedorServiceImpl(IProveedorRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                    "El repositorio no puede ser nulo."
            );
        }

        this.repository = repository;
    }


    @Override
    public Proveedor registrarProveedor(
            int idProveedor,
            String nombre,
            String documento,
            String telefono,
            String correo,
            String direccion
    ) {
        // 1. Validar el identificador.
        if (idProveedor <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser mayor que cero."
            );
        }

        // 2. Validar el nombre.
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del proveedor es obligatorio."
            );
        }

        // 3. Validar el documento.
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El documento del proveedor es obligatorio."
            );
        }

        // 4. Validar que el ID no exista.
        if (repository.buscarPorId(idProveedor) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un proveedor con ese identificador."
            );
        }

        // 5. Validar que el documento no esté duplicado.
        for (Proveedor existente : repository.obtenerTodos()) {
            if (existente.getDocumento() != null
                    && existente.getDocumento().trim()
                    .equalsIgnoreCase(documento.trim())) {
                throw new IllegalArgumentException(
                        "Ya existe un proveedor con ese documento."
                );
            }
        }

        // 6. Crear y guardar el proveedor.
        Proveedor proveedor = new Proveedor(
                idProveedor,
                nombre.trim(),
                documento.trim(),
                telefono,
                correo,
                direccion
        );

        repository.guardar(proveedor);

        return proveedor;
    }

    @Override
    public Proveedor buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    @Override
    public List<Proveedor> obtenerTodos() {
        return repository.obtenerTodos();
    }

}
