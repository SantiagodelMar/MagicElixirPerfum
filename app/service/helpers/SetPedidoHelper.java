package service.helpers;

import domain.enums.CanalVenta;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;
import service.validations.FormTypeValidator;

public class SetPedidoHelper {

    // HU-12: el vendedor escoge como se pago
    public static MetodoDePago getMetodoPago() {
        MetodoDePago metodo = null;

        while (metodo == null) {
            System.out.println("Seleccione el metodo de pago:");
            System.out.println("1. Efectivo (contra entrega)");
            System.out.println("2. PSE");
            System.out.println("3. Tarjeta de credito");
            System.out.println("4. Tarjeta de debito");
            int opcion = FormTypeValidator.intValidator("Opcion: ");

            switch (opcion) {
                case 1:
                    metodo = MetodoDePago.EFECTIVO;
                    break;
                case 2:
                    metodo = MetodoDePago.PSE;
                    break;
                case 3:
                    metodo = MetodoDePago.T_CREDITO;
                    break;
                case 4:
                    metodo = MetodoDePago.T_DEBITO;
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo");
            }
        }

        return metodo;
    }

    public static EstadoPedido getEstadoPedido() {
        EstadoPedido estado = null;

        while (estado == null) {
            System.out.println("Seleccione el nuevo estado del pedido:");
            System.out.println("1. Pendiente");
            System.out.println("2. Pagado");
            System.out.println("3. Enviado");
            System.out.println("4. Entregado");
            System.out.println("5. Cancelado");
            int opcion = FormTypeValidator.intValidator("Opcion: ");

            switch (opcion) {
                case 1:
                    estado = EstadoPedido.PENDIENTE;
                    break;
                case 2:
                    estado = EstadoPedido.PAGADO;
                    break;
                case 3:
                    estado = EstadoPedido.ENVIADO;
                    break;
                case 4:
                    estado = EstadoPedido.ENTREGADO;
                    break;
                case 5:
                    estado = EstadoPedido.CANCELADO;
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo");
            }
        }

        return estado;
    }

    // HU-24: por que canal llego la venta
    public static CanalVenta getCanalVenta() {
        CanalVenta canal = null;

        while (canal == null) {
            System.out.println("Seleccione el canal de venta:");
            System.out.println("1. Instagram");
            System.out.println("2. WhatsApp");
            System.out.println("3. Local fisico");
            int opcion = FormTypeValidator.intValidator("Opcion: ");

            switch (opcion) {
                case 1:
                    canal = CanalVenta.INSTAGRAM;
                    break;
                case 2:
                    canal = CanalVenta.WHATSAPP;
                    break;
                case 3:
                    canal = CanalVenta.LOCAL;
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo");
            }
        }

        return canal;
    }

}
