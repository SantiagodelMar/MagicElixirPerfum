package app.main;

import app.domain.Categoria;
import app.domain.Perfume;
import app.domain.Inventario;
import app.domain.EstadoPedido;
import app.domain.MetodoPago;
import app.domain.DetallePedido;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA MAGIC ELIXIR PARFUM ===");

        // 1. Crear Categoria
        Categoria catArabe = new Categoria(1, "Perfumería Árabe Masculina", "Fragancias orientales intensas y duraderas");

        // 2. Crear Perfume asociado a la Categoría
        Perfume perfume = new Perfume(101, "Khamrah", "Lattafa", 180000.0, 100, "Dulce, Amaderado, Canela", catArabe);

        // 3. Crear Inventario para el Perfume
        Inventario inventario = new Inventario(1, perfume, 10, "Disponible");

        // 4. Crear Estado de Pedido y Método de Pago
        EstadoPedido estado = new EstadoPedido(1, "Pendiente", "El pedido ha sido registrado y está a la espera de pago");
        MetodoPago pago = new MetodoPago(1, "Transferencia Bancaria", true);

        // 5. Crear Detalle de Pedido asociando el Perfume
        DetallePedido detalle = new DetallePedido(1, perfume, 2, perfume.getPrecio());

        // Imprimir comprobación
        Arrays.asList("Producto: " + detalle.getPerfume().getNombre() + " (" + detalle.getPerfume().getCategoria().getNombre() + ")", "Cantidad: " + detalle.getCantidad(), "Precio Unitario: $" + detalle.getPrecioUnitario(), "Subtotal: $" + detalle.getSubtotal(), "Estado Inicial: " + estado.getNombreEstado(), "Método de Pago: " + pago.getTipoPago(), "Stock en inventario: " + inventario.getStockDisponible() + " unidades (" + inventario.getEstadoDisponibilidad() + ")").forEach(System.out::println);
    }
}
