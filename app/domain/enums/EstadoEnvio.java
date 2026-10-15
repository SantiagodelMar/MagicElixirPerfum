package domain.enums;

public enum EstadoEnvio {

    CREADO("Creado"),
    EN_CAMINO("En camino"),
    ENTREGADO("Entregado");

    private final String descripcion;

    EstadoEnvio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}//Fin public enum
