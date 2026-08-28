package domain;

public class DetallePedido {

    //Atributos

    private int idDetallePedido;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    //Constructores

    public DetallePedido() {

    }

    public DetallePedido(int idDetallePedido, int cantidad, double precioUnitario, double subtotal) {
        this.idDetallePedido = idDetallePedido;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    //Getters and Setters

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }


    //Metodos

    public void create(){

    }

    public void agregarDetalle(){

    }

    public void calcularSubtotal(){

    }


}//Fin public class
