package domain.enums;

public enum EstadoInventario {

    DISPONIBLE("Disponible"),
    BAJO_STOCK("Bajo stock"),
    AGOTADO("Agotado");

    private final String descripcion;

    EstadoInventario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}//Fin public enum
