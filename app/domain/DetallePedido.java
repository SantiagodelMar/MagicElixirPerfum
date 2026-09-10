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

<<<<<<< HEAD
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
=======
    private int idDetallePedido;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
>>>>>>> ad4393e56ac4c46f9f4e2cb2390d508c1aa2fb10

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

<<<<<<< HEAD
    public Perfume getPerfume() {
        return perfume;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
=======
    public DetallePedido() {

    }

    public DetallePedido(int idDetallePedido, int cantidad, double precioUnitario, double subtotal) {
        this.idDetallePedido = idDetallePedido;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
>>>>>>> ad4393e56ac4c46f9f4e2cb2390d508c1aa2fb10
    }

    public int getCantidad() {
        return cantidad;
    }

<<<<<<< HEAD
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }
=======
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

>>>>>>> ad4393e56ac4c46f9f4e2cb2390d508c1aa2fb10

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal();
    }

<<<<<<< HEAD
    public double getSubtotal() {
        return subtotal;
    }

    // Métodos
    public double calcularSubtotal() {
        return this.cantidad * this.precioUnitario;
    }
}
=======
    public void create(){

    }

    public void agregarDetalle(){

    }

    public void calcularSubtotal(){

    }


}//Fin public class
>>>>>>> ad4393e56ac4c46f9f4e2cb2390d508c1aa2fb10
