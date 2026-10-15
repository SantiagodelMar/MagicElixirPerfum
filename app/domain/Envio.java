package domain;

import domain.enums.EstadoEnvio;

import java.time.LocalDate;

public class Envio {

    //Atributos

    private int idEnvio;
    private Pedido pedido;
    private String transportadora;
    private String numeroDeGuia;
    private String direccionDestino;
    private LocalDate fechaEnvio;
    private LocalDate fechaEntrega;
    private double costoEnvio;
    private EstadoEnvio estadoEnvio;

    //Constructores

    public Envio() {
    }

    public Envio(int idEnvio, Pedido pedido, String transportadora, String numeroDeGuia, String direccionDestino, LocalDate fechaEnvio, double costoEnvio, EstadoEnvio estadoEnvio) {
        this.idEnvio = idEnvio;
        this.pedido = pedido;
        this.transportadora = transportadora;
        this.numeroDeGuia = numeroDeGuia;
        this.direccionDestino = direccionDestino;
        this.fechaEnvio = fechaEnvio;
        this.costoEnvio = costoEnvio;
        this.estadoEnvio = estadoEnvio;
    }

    //Getters and Setters

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String getNumeroDeGuia() {
        return numeroDeGuia;
    }

    public void setNumeroDeGuia(String numeroDeGuia) {
        this.numeroDeGuia = numeroDeGuia;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public LocalDate getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDate fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public EstadoEnvio getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(EstadoEnvio estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

}
