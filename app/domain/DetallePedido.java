package app.domain;

public class DetallePedido {
    private int idDetalle;
    private Perfume perfume; // Conexión con Perfume
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    // Constructor vacío
    public DetallePedido(int idDetalle, app.domain.Perfume perfume, int cantidad, double precio) {
    }

    // Constructor con parámetros
    public DetallePedido(int idDetalle, Perfume perfume, int cantidad, double precioUnitario) {
        this.idDetalle = idDetalle;
        this.perfume = perfume;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal();
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
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

    // Métodos
    public double calcularSubtotal() {
        return this.cantidad * this.precioUnitario;
    }
}
