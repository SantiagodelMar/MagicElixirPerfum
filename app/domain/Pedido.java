package domain;

import domain.enums.CanalVenta;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    //Atributos

    private int idPedido;
    private LocalDate fechaPedido;
    private Cliente cliente;
    private double totalCostoPedido;
    private String observaciones;
    private List<DetallePedido> detalles;
    private EstadoPedido estadoPedido;
    private MetodoDePago metodoPago;
    private CanalVenta canalVenta;

    //Constructores

    public Pedido() {
        this.detalles = new ArrayList<>();
    }

    public Pedido(int idPedido, LocalDate fechaPedido, Cliente cliente, String observaciones, EstadoPedido estadoPedido, MetodoDePago metodoPago, CanalVenta canalVenta) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.cliente = cliente;
        this.observaciones = observaciones;
        this.estadoPedido = estadoPedido;
        this.metodoPago = metodoPago;
        this.canalVenta = canalVenta;
        this.totalCostoPedido = 0;
        this.detalles = new ArrayList<>();
    }

    //Getters and Setters

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public MetodoDePago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoDePago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public CanalVenta getCanalVenta() {
        return canalVenta;
    }

    public void setCanalVenta(CanalVenta canalVenta) {
        this.canalVenta = canalVenta;
    }

    // Dos objetos son "el mismo" si tienen el mismo id (asi un objeto leido otra vez de la base de datos
    // se reconoce como igual al que ya estaba, por ejemplo en las listas desplegables)
    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null || getClass() != otro.getClass()) {
            return false;
        }
        return idPedido == ((Pedido) otro).idPedido;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idPedido);
    }

}
