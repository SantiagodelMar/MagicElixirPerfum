package domain;

public class MetodoDePago {

    //Atributos

    private int idMetodoPago;
    private String nombreMetodoPago;
    private String descripcionMetodoPago;


    //Constructores

    public MetodoDePago() {

    }


    public MetodoDePago(int idMetodoPago, String nombreMetodoPago, String descripcionMetodoPago) {
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodoPago = nombreMetodoPago;
        this.descripcionMetodoPago = descripcionMetodoPago;
    }

    //Getters and Setters

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombreMetodoPago() {
        return nombreMetodoPago;
    }

    public void setNombreMetodoPago(String nombreMetodoPago) {
        this.nombreMetodoPago = nombreMetodoPago;
    }

    public String getDescripcionMetodoPago() {
        return descripcionMetodoPago;
    }

    public void setDescripcionMetodoPago(String descripcionMetodoPago) {
        this.descripcionMetodoPago = descripcionMetodoPago;
    }


    //Metodos

    public void validarPago(){

    }


}//Fin public class
