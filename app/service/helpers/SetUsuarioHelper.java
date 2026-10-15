package service.helpers;

import domain.enums.TipoDocumento;
import service.validations.FormTypeValidator;

public class SetUsuarioHelper {

    public static TipoDocumento getTipoDocumento() {
        TipoDocumento tipoDoc = null;

        while (tipoDoc == null) {
            System.out.println("Seleccione el tipo de documento:");
            System.out.println("1. Cedula de ciudadania");
            System.out.println("2. Cedula de extranjeria");
            System.out.println("3. Tarjeta de identidad");
            System.out.println("4. Pasaporte");
            int opcion = FormTypeValidator.intValidator("Opcion: ");

            switch (opcion) {
                case 1:
                    tipoDoc = TipoDocumento.CC;
                    break;
                case 2:
                    tipoDoc = TipoDocumento.CE;
                    break;
                case 3:
                    tipoDoc = TipoDocumento.TI;
                    break;
                case 4:
                    tipoDoc = TipoDocumento.PASAPORTE;
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo");
            }
        }

        return tipoDoc;
    }

}