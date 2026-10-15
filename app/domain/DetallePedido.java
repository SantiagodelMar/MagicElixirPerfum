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

    public DetallePedido(int idDetallePedido, Perfume perfume, int cantidad, double precioUnitario, double subtotal) {
        this.idDetallePedido = idDetallePedido;
        this.perfume = perfume;
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

    public Perfume getPerfume() {
        return perfume;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
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

}