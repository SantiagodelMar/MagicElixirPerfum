package app.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Devolucion {

    // Atributos de las clases

    private static final int DIAS_MAXIMOS_PARA_DEVOLVER = 30;
    private static final double PORCENTAJE_REEMBOLSO_PARCIAL = 0.80;

    private int idDevolucion;
    private Pedido pedido;
    private LocalDate fechaSolicitud;
    private MotivoDevolucion motivo;
    private String descripcion;
    private EstadoDevolucion estado;
    private double montoReembolso;

    public Devolucion() {
        this.estado = EstadoDevolucion.SOLICITADA;
    }

    public Devolucion(int idDevolucion, Pedido pedido, LocalDate fechaSolicitud,
                      MotivoDevolucion motivo, String descripcion) {
        this.idDevolucion = idDevolucion;
        this.pedido = pedido;
        this.fechaSolicitud = fechaSolicitud;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.estado = EstadoDevolucion.SOLICITADA;
        this.montoReembolso = 0;
    }

    // Getters and setters del sistema MagicElicirPerfum

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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

    public EstadoDevolucion getEstado() {
        return estado;
    }

    public double getMontoReembolso() {
        return montoReembolso;
    }

    // Méodos del negocio

    public boolean estaDentroDelPlazo(LocalDate fechaEntrega) {
        if (fechaEntrega == null || fechaSolicitud == null) {
            return false;
        }
        long dias = ChronoUnit.DAYS.between(fechaEntrega, fechaSolicitud);
        return dias >= 0 && dias <= DIAS_MAXIMOS_PARA_DEVOLVER;
    }

    public double calcularMontoReembolso(double totalPagado) {
        if (motivo == MotivoDevolucion.ARREPENTIMIENTO_DE_COMPRA) {
            return totalPagado * PORCENTAJE_REEMBOLSO_PARCIAL;
        }
        return totalPagado;
    }

    public void aprobar(double totalPagado) {
        this.estado = EstadoDevolucion.APROBADA;
        this.montoReembolso = calcularMontoReembolso(totalPagado);
    }

    public void rechazar() {
        this.estado = EstadoDevolucion.RECHAZADA;
        this.montoReembolso = 0;
    }

    public void registrarReembolso() {
        this.estado = EstadoDevolucion.REEMBOLSADA;
    }

    public String mostrarDetalle() {
        return "Devolucion #" + idDevolucion
                + " | Motivo: " + motivo
                + " | Estado: " + estado
                + " | Reembolso: $" + montoReembolso;
    }
}
