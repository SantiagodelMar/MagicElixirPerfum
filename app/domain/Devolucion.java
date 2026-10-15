package domain;

import domain.enums.EstadoDevolucion;
import domain.enums.MotivoDevolucion;

import java.time.LocalDate;

public class Devolucion {

    //Atributos

    private int idDevolucion;
    private int idPedido;
    private LocalDate fechaEntrega;
    private LocalDate fechaSolicitud;
    private MotivoDevolucion motivo;
    private String descripcion;
    private double totalPagado;
    private EstadoDevolucion estado;
    private double montoReembolso;

    //Constructores

    public Devolucion() {
    }

    public Devolucion(int idDevolucion, int idPedido, LocalDate fechaEntrega, LocalDate fechaSolicitud,
                      MotivoDevolucion motivo, String descripcion, double totalPagado) {
        this.idDevolucion = idDevolucion;
        this.idPedido = idPedido;
        this.fechaEntrega = fechaEntrega;
        this.fechaSolicitud = fechaSolicitud;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.totalPagado = totalPagado;
        this.estado = EstadoDevolucion.SOLICITADA;
        this.montoReembolso = 0;
    }

    //Getters and Setters

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public MotivoDevolucion getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoDevolucion motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public EstadoDevolucion getEstado() {
        return estado;
    }

    public void setEstado(EstadoDevolucion estado) {
        this.estado = estado;
    }

    public double getMontoReembolso() {
        return montoReembolso;
    }

    public void setMontoReembolso(double montoReembolso) {
        this.montoReembolso = montoReembolso;
    }

}
