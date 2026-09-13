package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {

    //Atributos

    private int idPedido;
    private Date fechaPedido;
    private Cliente cliente;
    private double totalCostoPedido;
    private String observaciones;
    private List <DetallePedido> detalles;
    private EstadoPedido estadoPedido;
    private MetodoDePago metodoPago;


    //Constructores

    //CONSTRUCTOR VACÍO

    public Pedido() {
        this.detalles = new ArrayList<>();
        this.fechaPedido = new Date();
        this.estadoPedido = EstadoPedido.PENDIENTE;
    }

    // CONSTRUCTOR CON PARÁMETROS INICIALES

    public Pedido(int idPedido, Cliente cliente, MetodoDePago metodoPago, String observaciones) {
        this(); // Ejecuta el constructor vacío para inicializar la lista, la fecha y el estado
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
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

}//Fin public class
