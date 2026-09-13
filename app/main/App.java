package main;

import domain.*;
import repository.IPedidoRepository;
import repository.PedidoRepositoryImpl;
import service.IPedidoService;
import service.PedidoServiceImpl;

public class App {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SISTEMA DE GESTIÓN - MAGIC ELIXIR PARFUM 🧪✨  ");
        System.out.println("==================================================\n");

        // 1. Inicialización de Capas (Inyección de Dependencias manual)
        IPedidoRepository pedidoRepository = new PedidoRepositoryImpl();
        IPedidoService pedidoService = new PedidoServiceImpl(pedidoRepository);

        // 2. Creación de Datos de Prueba (Clientes y Catálogo)
        Cliente cliente = new Cliente(1, "Anyi Cañas", "anyi@gmail.com", "3001234567", "Calle 10 #20-30");
        Perfume perfume1 = new Perfume(101, "Khamrah", "Lattafa", 180000.0, 10, "Disponible");
        Perfume perfume2 = new Perfume(102, "Club de Nuit Intense", "Armaf", 220000.0, 5, "Disponible");

        // 3. Flujo de Negocio: Crear un nuevo Pedido
        System.out.println("--- 1. Creando Pedido ---");
        Pedido nuevoPedido = pedidoService.crearPedido(
                1001,
                cliente,
                MetodoDePago.PSE,
                "Dejar en la portería del edificio"
        );

        // 4. Agregar Productos al Pedido
        System.out.println("\n--- 2. Agregando Perfumes al Carrito ---");
        pedidoService.agregarDetalle(nuevoPedido, perfume1, 2); // 2 unidades de Khamrah ($360,000)
        pedidoService.agregarDetalle(nuevoPedido, perfume2, 1); // 1 unidad de Club de Nuit ($220,000)

        // 5. Imprimir Resumen en Consola
        System.out.println("\n--- 3. Resumen del Pedido ---");
        System.out.println("ID Pedido: #" + nuevoPedido.getIdPedido());
        System.out.println("Cliente: " + nuevoPedido.getCliente().getNombre());
        System.out.println("Método de Pago: " + nuevoPedido.getMetodoPago().getDescripcion());
        System.out.println("Estado Actual: " + nuevoPedido.getEstadoPedido().getDescripcion());
        System.out.println("\nDetalles de la Compra:");
        for (DetallePedido dp : nuevoPedido.getDetalles()) {
            System.out.println(" - " + dp.getPerfume().getNombre() +
                    " x" + dp.getCantidad() +
                    " | Unitario: $" + dp.getPrecioUnitario() +
                    " | Subtotal: $" + dp.getSubtotal());
        }
        System.out.println("TOTAL A PAGAR: $" + nuevoPedido.getTotalCostoPedido());

        // 6. Confirmación y Guardado en Repositorio
        System.out.println("\n--- 4. Confirmando y Guardando Pedido ---");
        boolean confirmado = pedidoService.confirmarPedido(nuevoPedido);

        if (confirmado) {
            System.out.println("\nStatus Final: El pedido cambia a estado -> " + nuevoPedido.getEstadoPedido().getDescripcion());
        }

        // 7. Verificar consulta al Repositorio
        System.out.println("\n--- 5. Verificación en Base de Datos / Repositorio ---");
        Pedido pedidoConsultado = pedidoRepository.buscarPorId(1001);
        if (pedidoConsultado != null) {
            System.out.println("✅ Pedido #" + pedidoConsultado.getIdPedido() + " recuperado exitosamente del Repositorio.");
        }
    }

}//Fin public class
