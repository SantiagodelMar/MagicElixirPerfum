package domain;

public enum MetodoDePago {

    EFECTIVO("Pago contra entrega en efectivo"),
    PSE("Pago por PSE"),
    T_CREDITO("Pago con tarjeta de crédito"),
    T_DEBITO("Pago con tarjeta de débito");

    private final String descripcion;

    MetodoDePago(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }

}//Fin public
