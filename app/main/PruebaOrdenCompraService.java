
package main;

import domain.OrdenCompra;
import domain.Proveedor;
import repository.IOrdenCompraRepository;
import repository.IProveedorRepository;
import repository.OrdenCompraRepositoryImpl;
import repository.ProveedorRepositoryImpl;
import service.OrdenCompraServiceImpl;

public class PruebaOrdenCompraService {

    public static void main(String[] args) {

        // Preparar los repositorios.
        IOrdenCompraRepository ordenRepository =
                new OrdenCompraRepositoryImpl();

        IProveedorRepository proveedorRepository =
                new ProveedorRepositoryImpl();

        // Registrar un proveedor activo para las pruebas.
        Proveedor proveedorActivo = new Proveedor(
                1,
                "Proveedor de prueba",
                "123456789",
                "3001234567",
                "proveedor@prueba.com",
                "Calle de prueba"
        );

        proveedorRepository.guardar(proveedorActivo);

        // Registrar un proveedor inactivo.
        Proveedor proveedorInactivo = new Proveedor(
                2,
                "Proveedor inactivo",
                "987654321",
                "3007654321",
                "inactivo@prueba.com",
                "Carrera de prueba"
        );

        proveedorInactivo.setActivo(false);
        proveedorRepository.guardar(proveedorInactivo);

        // Crear el servicio con sus dependencias.
        OrdenCompraServiceImpl servicio =
                new OrdenCompraServiceImpl(
                        ordenRepository,
                        proveedorRepository
                );

        System.out.println("=== PRUEBAS DEL SERVICIO ===");

        // Prueba 1: registrar una orden válida.
        try {
            OrdenCompra orden = servicio.registrarOrdenCompra(
                    100, 1, 150000, "Compra de prueba"
            );

            System.out.println(
                    "1. Orden registrada: "
                            + (orden.getIdOrdenCompra() == 100)
            );
        } catch (IllegalArgumentException e) {
            System.out.println("1. ERROR: " + e.getMessage());
        }

        // Prueba 2: consultar la orden.
        OrdenCompra encontrada = servicio.buscarPorId(100);

        System.out.println(
                "2. Orden encontrada: " + (encontrada != null)
        );

        // Prueba 3: rechazar un proveedor inexistente.
        try {
            servicio.registrarOrdenCompra(
                    101, 999, 50000, "Proveedor inexistente"
            );

            System.out.println("3. ERROR: Se aceptó un proveedor inexistente.");
        } catch (IllegalArgumentException e) {
            System.out.println("3. Proveedor inexistente rechazado correctamente.");
        }

        // Prueba 4: rechazar un total igual a cero.
        try {
            servicio.registrarOrdenCompra(
                    102, 1, 0, "Total inválido"
            );

            System.out.println("4. ERROR: Se aceptó un total de cero.");
        } catch (IllegalArgumentException e) {
            System.out.println("4. Total inválido rechazado correctamente.");
        }

        // Prueba 5: rechazar un identificador duplicado.
        try {
            servicio.registrarOrdenCompra(
                    100, 1, 80000, "Orden duplicada"
            );

            System.out.println("5. ERROR: Se aceptó una orden duplicada.");
        } catch (IllegalArgumentException e) {
            System.out.println("5. Orden duplicada rechazada correctamente.");
        }

        // Prueba 6: rechazar un proveedor inactivo.
        try {
            servicio.registrarOrdenCompra(
                    103, 2, 60000, "Proveedor inactivo"
            );

            System.out.println("6. ERROR: Se aceptó un proveedor inactivo.");
        } catch (IllegalArgumentException e) {
            System.out.println("6. Proveedor inactivo rechazado correctamente.");
        }

        // Comprobar que solo se guardó la orden válida.
        System.out.println(
                "Órdenes registradas: "
                        + servicio.obtenerTodos().size()
        );
    }
}
