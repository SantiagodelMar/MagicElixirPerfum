package service.helpers;

import domain.Pedido;

public class PedidoUtil {

    // Constructor privado para evitar que alguien instancie esta clase con 'new'
    private PedidoUtil() {
        throw new UnsupportedOperationException("Clase utilitaria no instanciable");
    }

    // Formatea números a texto de moneda local
    public static String formatearMoneda(double monto) {
        return String.format("$%,.2f COP", monto);
    }

    // Valida que el pedido exista y tenga al menos un producto agregado
    public static boolean esPedidoValido(Pedido pedido) {
        return pedido != null && pedido.getDetalles() != null && !pedido.getDetalles().isEmpty();
    }


}
