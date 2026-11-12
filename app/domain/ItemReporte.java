package domain;

// Una fila de un reporte (HU-23 y HU-24): un nombre, cuantas unidades/pedidos y cuanto dinero
public class ItemReporte {

    //Atributos
    private String nombre;
    private int cantidad;
    private double total;

    //Constructores
    public ItemReporte(String nombre, int cantidad, double total) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.total = total;
    }

    //Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}//Fin public class
