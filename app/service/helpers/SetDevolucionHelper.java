package service.helpers;

import domain.enums.MotivoDevolucion;
import service.validations.FormTypeValidator;

public class SetDevolucionHelper {

    public static MotivoDevolucion getMotivo() {
        MotivoDevolucion motivo = null;

        while (motivo == null) {
            System.out.println("Seleccione el motivo de la devolucion:");
            System.out.println("1. Producto defectuoso");
            System.out.println("2. Producto equivocado");
            System.out.println("3. Afectado en el envio");
            System.out.println("4. Arrepentimiento de compra (se devuelve el 80%)");
            int opcion = FormTypeValidator.intValidator("Opcion: ");

            switch (opcion) {
                case 1:
                    motivo = MotivoDevolucion.PRODUCTO_DEFECTUOSO;
                    break;
                case 2:
                    motivo = MotivoDevolucion.PRODUCTO_EQUIVOCADO;
                    break;
                case 3:
                    motivo = MotivoDevolucion.AFECTADO_EN_EL_ENVIO;
                    break;
                case 4:
                    motivo = MotivoDevolucion.ARREPENTIMIENTO_DE_COMPRA;
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo");
            }
        }

        return motivo;
    }

}
