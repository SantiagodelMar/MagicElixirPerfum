package gui.validaciones;

import gui.componentes.CampoClave;
import gui.componentes.CampoTexto;
import gui.componentes.Combo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Es el "FormTypeValidator" pero para las ventanas.
// En la consola se repetia la pregunta con un while; aqui se marca el campo en rojo
// y se lanza DatoInvalidoException con el mensaje para el usuario.
public class ValidadorCampos {

    public static String texto(CampoTexto campo, String nombreCampo) {
        String valor = campo.getTexto();
        if (valor.isEmpty()) {
            campo.marcarError();
            throw new DatoInvalidoException("El campo " + nombreCampo + " no puede estar vacío");
        }
        return valor;
    }

    // Para los id, cantidades y mililitros (numeros enteros mayores a 0)
    public static int entero(CampoTexto campo, String nombreCampo) {
        String valor = texto(campo, nombreCampo);
        try {
            int numero = Integer.parseInt(valor);
            if (numero <= 0) {
                campo.marcarError();
                throw new DatoInvalidoException(nombreCampo + " debe ser mayor a 0");
            }
            return numero;
        } catch (NumberFormatException e) {
            campo.marcarError();
            throw new DatoInvalidoException(nombreCampo + " solo admite números enteros");
        }
    }

    // Para el stock, que si puede ser 0
    public static int enteroConCero(CampoTexto campo, String nombreCampo) {
        String valor = texto(campo, nombreCampo);
        try {
            int numero = Integer.parseInt(valor);
            if (numero < 0) {
                campo.marcarError();
                throw new DatoInvalidoException(nombreCampo + " no puede ser negativo");
            }
            return numero;
        } catch (NumberFormatException e) {
            campo.marcarError();
            throw new DatoInvalidoException(nombreCampo + " solo admite números enteros");
        }
    }

    // Acepta "240000", "240.000" o "$ 240.000" (los puntos son de miles en Colombia)
    public static double dinero(CampoTexto campo, String nombreCampo) {
        String valor = texto(campo, nombreCampo)
                .replace("$", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", ".");
        try {
            double numero = Double.parseDouble(valor);
            if (numero <= 0) {
                campo.marcarError();
                throw new DatoInvalidoException(nombreCampo + " debe ser mayor a 0");
            }
            return numero;
        } catch (NumberFormatException e) {
            campo.marcarError();
            throw new DatoInvalidoException(nombreCampo + " debe ser un valor en pesos, ej: 240000");
        }
    }

    public static String correo(CampoTexto campo) {
        String valor = texto(campo, "Correo");
        int arroba = valor.indexOf("@");
        if (arroba <= 0 || valor.lastIndexOf(".") < arroba || valor.contains(" ")) {
            campo.marcarError();
            throw new DatoInvalidoException("El correo no es válido, ej: nombre@gmail.com");
        }
        return valor;
    }

    // Celular o telefono: solo numeros, entre 7 y 10 digitos
    public static String telefono(CampoTexto campo, String nombreCampo) {
        String valor = texto(campo, nombreCampo);
        if (!valor.matches("[0-9]{7,10}")) {
            campo.marcarError();
            throw new DatoInvalidoException(nombreCampo + " debe tener entre 7 y 10 números");
        }
        return valor;
    }

    public static LocalDate fecha(CampoTexto campo, String nombreCampo) {
        String valor = texto(campo, nombreCampo);
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            campo.marcarError();
            throw new DatoInvalidoException(nombreCampo + " debe tener el formato aaaa-mm-dd, ej: 2001-03-15");
        }
    }

    public static String clave(CampoClave campo) {
        String valor = campo.getClave();
        if (valor.length() < 4) {
            campo.marcarError();
            throw new DatoInvalidoException("La contraseña debe tener mínimo 4 caracteres");
        }
        return valor;
    }

    public static <T> T seleccion(Combo<T> combo, String nombreCampo) {
        T valor = combo.getSeleccionado();
        if (valor == null) {
            combo.marcarError();
            throw new DatoInvalidoException("Seleccione " + nombreCampo);
        }
        return valor;
    }

}//Fin public class
