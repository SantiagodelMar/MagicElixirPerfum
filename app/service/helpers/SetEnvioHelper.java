package service.helpers;

import domain.enums.EstadoEnvio;
import service.validations.FormTypeValidator;

public class SetEnvioHelper {

    // HU-14: actualizar el estado del envio
    public static EstadoEnvio getEstadoEnvio() {
        EstadoEnvio estado = null;

        while (estado == null) {
            System.out.println("Seleccione el estado del envio:");
            System.out.println("1. Creado");
            System.out.println("2. En camino");
            System.out.println("3. Entregado");
            int opcion = FormTypeValidator.intValidator("Opcion: ");

            switch (opcion) {
                case 1:
                    estado = EstadoEnvio.CREADO;
                    break;
                case 2:
                    estado = EstadoEnvio.EN_CAMINO;
                    break;
                case 3:
                    estado = EstadoEnvio.ENTREGADO;
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo");
            }
        }

        return estado;
    }

}
