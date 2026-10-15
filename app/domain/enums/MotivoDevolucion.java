package domain.enums;

public enum MotivoDevolucion {

    PRODUCTO_DEFECTUOSO("Producto defectuoso"),
    PRODUCTO_EQUIVOCADO("Producto equivocado"),
    AFECTADO_EN_EL_ENVIO("Afectado en el envio"),
    ARREPENTIMIENTO_DE_COMPRA("Arrepentimiento de compra");

    private final String descripcion;

    MotivoDevolucion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}//Fin public enum
