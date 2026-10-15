
package domain.enums;

public enum EstadoOrdenCompra {

    PENDIENTE("Pendiente"),
    APROBADA("Aprobada"),
    RECIBIDA("Recibida"),
    CANCELADA("Cancelada");

    // Atributos
    private final String descripcion;

    // Constructor
    EstadoOrdenCompra(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter
    public String getDescripcion() {
        return descripcion;
    }
}