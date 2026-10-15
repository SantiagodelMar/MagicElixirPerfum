package domain.enums;

public enum TipoDocumento {

    CC("Cedula de ciudadania"),
    CE("Cedula de extranjeria"),
    TI("Tarjeta de identidad"),
    PASAPORTE("Pasaporte");

    private final String descripcion;

    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}//Fin public enum
