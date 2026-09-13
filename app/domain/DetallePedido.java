package domain;

public class DetallePedido {

    //Atributos

    private int idDetallePedido;
    private Perfume perfume;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;


    //Constructores

    public DetallePedido() {

    }

    public DetallePedido(int idDetallePedido, Perfume perfume, int cantidad) {
        this.idDetallePedido = idDetallePedido;
        this.perfume = perfume;
        this.cantidad = cantidad;
        this.precioUnitario = (perfume!= null) ? perfume.getPrecio() : 0.0;
        this.subtotal = calcularSubtotal();
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
        this.subtotal = calcularSubtotal();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    // NOTA: Se eliminó setSubtotal() para proteger la integridad de las cuentas.

    // Métod de cálculo interno simple
    public double calcularSubtotal() {
        return this.precioUnitario * this.cantidad;
    }

}//Fin public class
