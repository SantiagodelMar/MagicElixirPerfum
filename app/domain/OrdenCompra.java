package domain;

import domain.enums.EstadoOrdenCompra;

import java.time.LocalDate;

public class OrdenCompra {

    //Atributos

    private int idOrdenCompra;
    private Proveedor proveedor;
    private LocalDate fechaOrden;
    private EstadoOrdenCompra estado;
    private double total;
    private String observaciones;

    //Constructores

    public OrdenCompra() {
    }

    public OrdenCompra(int idOrdenCompra, Proveedor proveedor, LocalDate fechaOrden, EstadoOrdenCompra estado, double total, String observaciones) {
        this.idOrdenCompra = idOrdenCompra;
        this.proveedor = proveedor;
        this.fechaOrden = fechaOrden;
        this.estado = estado;
        this.total = total;
        this.observaciones = observaciones;
    }

    //Getters and Setters

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
