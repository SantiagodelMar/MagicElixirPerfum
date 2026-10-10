
package main;

import domain.Proveedor;
import repository.IProveedorRepository;
import repository.ProveedorRepositoryImpl;
import service.ProveedorServiceImpl;

public class PruebaProveedorService {

    public static void main(String[] args) {

        IProveedorRepository repository =
                new ProveedorRepositoryImpl();

        ProveedorServiceImpl servicio =
                new ProveedorServiceImpl(repository);

        System.out.println("=== PRUEBAS DE PROVEEDORES ===");

        // Prueba 1: registrar un proveedor válido.
        Proveedor proveedor = servicio.registrarProveedor(
                1,
                "Proveedor de prueba",
                "123456789",
                "3001234567",
                "prueba@correo.com",
                "Calle de prueba"
        );

        System.out.println(
                "1. Proveedor registrado: " + (proveedor != null)
        );

        // Prueba 2: buscar el proveedor registrado.
        System.out.println(
                "2. Proveedor encontrado: "
                        + (servicio.buscarPorId(1) != null)
        );

        // Prueba 3: rechazar un identificador duplicado.
        try {
            servicio.registrarProveedor(
                    1,
                    "Otro proveedor",
                    "987654321",
                    "3000000000",
                    "otro@correo.com",
                    "Otra dirección"
            );

            System.out.println(
                    "3. ERROR: se aceptó un ID duplicado."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "3. ID duplicado rechazado correctamente."
            );
        }

        // Prueba 4: rechazar un nombre vacío.
        try {
            servicio.registrarProveedor(
                    2,
                    " ",
                    "555555555",
                    "3000000000",
                    "vacio@correo.com",
                    "Otra dirección"
            );

            System.out.println(
                    "4. ERROR: se aceptó un nombre vacío."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "4. Nombre vacío rechazado correctamente."
            );
        }

        // Prueba 5: rechazar un documento vacío.
        try {
            servicio.registrarProveedor(
                    3,
                    "Proveedor sin documento",
                    " ",
                    "3000000000",
                    "sin-documento@correo.com",
                    "Otra dirección"
            );

            System.out.println(
                    "5. ERROR: se aceptó un documento vacío."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "5. Documento vacío rechazado correctamente."
            );
        }

        // Prueba 6: rechazar un documento duplicado.
        try {
            servicio.registrarProveedor(
                    4,
                    "Proveedor con documento repetido",
                    "123456789",
                    "3000000000",
                    "duplicado@correo.com",
                    "Otra dirección"
            );

            System.out.println(
                    "6. ERROR: se aceptó un documento duplicado."
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "6. Documento duplicado rechazado correctamente."
            );
        }

        System.out.println(
                "Total de proveedores: "
                        + servicio.obtenerTodos().size()
        );
    }
}
