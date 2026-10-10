
package domain;

import java.time.LocalDate;

public class OrdenCompra {

    // Atributos
    private int idOrdenCompra;
    private Proveedor proveedor;
    private LocalDate fechaOrden;
    private EstadoOrdenCompra estado;
    private double total;
    private String observaciones;

    // Constructores
    public OrdenCompra() {
        this.fechaOrden = LocalDate.now();
        this.estado = EstadoOrdenCompra.PENDIENTE;
    }

    public OrdenCompra(int idOrdenCompra, Proveedor proveedor,
                       LocalDate fechaOrden, double total,
                       String observaciones) {
        this.idOrdenCompra = idOrdenCompra;
        this.proveedor = proveedor;
        this.fechaOrden = fechaOrden;
        this.total = total;
        this.observaciones = observaciones;
        this.estado = EstadoOrdenCompra.PENDIENTE;
    }

    // Getters y setters
    public int getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(int idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public EstadoOrdenCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrdenCompra estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
