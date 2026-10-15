package service.helpers;

import domain.enums.EstadoOrdenCompra;
import service.validations.FormTypeValidator;

public class SetOrdenCompraHelper {

    public static EstadoOrdenCompra getEstadoOrdenCompra() {
        EstadoOrdenCompra estado = null;

        while (estado == null) {
            System.out.println("Seleccione el estado de la orden:");
            System.out.println("1. Pendiente");
            System.out.println("2. Aprobada");
            System.out.println("3. Recibida");
            System.out.println("4. Cancelada");
            int opcion = FormTypeValidator.intValidator("Opcion: ");

            switch (opcion) {
                case 1:
                    estado = EstadoOrdenCompra.PENDIENTE;
                    break;
                case 2:
                    estado = EstadoOrdenCompra.APROBADA;
                    break;
                case 3:
                    estado = EstadoOrdenCompra.RECIBIDA;
                    break;
                case 4:
                    estado = EstadoOrdenCompra.CANCELADA;
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo");
            }
        }

        return estado;
    }

}
