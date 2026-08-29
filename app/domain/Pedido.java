package domain;

import java.util.Date;

public class Pedido {

    //Atributos

    private int idPedido;
    private Date fechaPedido;
    private double totalCostoPedido;
    private String observaciones;

    //Constructores

    public Pedido() {
    }

    public Pedido(int idPedido, Date fechaPedido, double totalCostoPedido, String observaciones) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.totalCostoPedido = totalCostoPedido;
        this.observaciones = observaciones;
    }

    //Getters and Setters

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getTotalCostoPedido() {
        return totalCostoPedido;
    }

    public void setTotalCostoPedido(double totalCostoPedido) {
        this.totalCostoPedido = totalCostoPedido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }



    //Metodos

    public void create(){

    }

    public void agregarDetalle(){

    }

    public void calcularTotal(){

    }

    public void confirmarPedido(){

    }

    public void cancelarPedido(){

    }

    public void update(){

    }


}//Fin public class
