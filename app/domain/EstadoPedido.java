package domain;

public class EstadoPedido {

    //Atributos

    private int idEstadoPedido;
    private  String nombreEstadoPedido;
    private String descripcionEstadoPedido;


    //Constructores

    public EstadoPedido() {

    }

    public EstadoPedido(int idEstadoPedido, String nombreEstadoPedido, String descripcionEstadoPedido) {
        this.idEstadoPedido = idEstadoPedido;
        this.nombreEstadoPedido = nombreEstadoPedido;
        this.descripcionEstadoPedido = descripcionEstadoPedido;
    }

    //Getters and Setters

    public int getIdEstadoPedido() {
        return idEstadoPedido;
    }

    public void setIdEstadoPedido(int idEstadoPedido) {
        this.idEstadoPedido = idEstadoPedido;
    }

    public String getNombreEstadoPedido() {
        return nombreEstadoPedido;
    }

    public void setNombreEstadoPedido(String nombreEstadoPedido) {
        this.nombreEstadoPedido = nombreEstadoPedido;
    }

    public String getDescripcionEstadoPedido() {
        return descripcionEstadoPedido;
    }

    public void setDescripcionEstadoPedido(String descripcionEstadoPedido) {
        this.descripcionEstadoPedido = descripcionEstadoPedido;
    }


    //Metodos

    public void cambiarEstado(){

    }



}//Fin public class
