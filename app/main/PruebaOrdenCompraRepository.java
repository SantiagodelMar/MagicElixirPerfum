
package main;

import domain.OrdenCompra;
import domain.Proveedor;
import repository.OrdenCompraRepositoryImpl;

import java.time.LocalDate;

public class PruebaOrdenCompraRepository {

    public static void main(String[] args) {

        OrdenCompraRepositoryImpl repositorio =
                new OrdenCompraRepositoryImpl();

        Proveedor proveedor = new Proveedor(
                1,
                "Proveedor de prueba",
                "123456789",
                "3001234567",
                "proveedor@prueba.com",
                "Calle de prueba"
        );

        OrdenCompra orden = new OrdenCompra(
                100,
                proveedor,
                LocalDate.now(),
                150000,
                "Orden de prueba"
        );

        // 1. Guardar la orden.
        repositorio.guardar(orden);
        System.out.println("Orden guardada correctamente.");

        // 2. Buscar la orden por su identificador.
        OrdenCompra encontrada = repositorio.buscarPorId(100);
        System.out.println(
                "Orden encontrada: " + (encontrada != null)
        );

        // 3. Consultar todas las órdenes.
        System.out.println(
                "Cantidad de órdenes: " + repositorio.obtenerTodos().size()
        );

        // 4. Verificar que no permita identificadores duplicados.
        try {
            repositorio.guardar(orden);
            System.out.println("ERROR: Se permitió una orden duplicada.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Duplicado rechazado correctamente: " + e.getMessage()
            );
        }
    }
}
