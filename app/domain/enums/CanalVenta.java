package domain.enums;

// HU-24: por donde llego la venta (para saber donde invertir)
public enum CanalVenta {
    INSTAGRAM("Instagram"),
    WHATSAPP("WhatsApp"),
    LOCAL("Local fisico");

    private final String descripcion;

    CanalVenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}//Fin public enum
