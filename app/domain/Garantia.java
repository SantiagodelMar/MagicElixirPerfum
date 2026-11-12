package domain;

import java.time.LocalDate;

// HU-20 y HU-21: garantia de un pedido (fecha de inicio + meses de cobertura)
public class Garantia {

    //Atributos
    private int idGarantia;
    private Pedido pedido;
    private LocalDate fechaInicio;
    private int mesesCobertura;
    private String descripcion;

    //Constructores
    public Garantia() {
    }

    public Garantia(int idGarantia, Pedido pedido, LocalDate fechaInicio, int mesesCobertura, String descripcion) {
        this.idGarantia = idGarantia;
        this.pedido = pedido;
        this.fechaInicio = fechaInicio;
        this.mesesCobertura = mesesCobertura;
        this.descripcion = descripcion;
    }

    //Metodos
    // La garantia termina "mesesCobertura" meses despues de la fecha de inicio
    public LocalDate getFechaVencimiento() {
        return fechaInicio.plusMonths(mesesCobertura);
    }

    //Getters and Setters
    public int getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(int idGarantia) {
        this.idGarantia = idGarantia;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getMesesCobertura() {
        return mesesCobertura;
    }

    public void setMesesCobertura(int mesesCobertura) {
        this.mesesCobertura = mesesCobertura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}//Fin public class
