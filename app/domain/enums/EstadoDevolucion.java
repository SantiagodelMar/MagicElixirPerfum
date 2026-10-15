package domain.enums;

public enum EstadoDevolucion {

    SOLICITADA("Solicitada"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada"),
    REEMBOLSADA("Reembolsada");

    private final String descripcion;

    EstadoDevolucion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}//Fin public enum
