package gui.validaciones;

// Excepcion propia para cuando un campo del formulario esta mal diligenciado.
// El mensaje es el que se le muestra al usuario.
public class DatoInvalidoException extends RuntimeException {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }

}//Fin public class
