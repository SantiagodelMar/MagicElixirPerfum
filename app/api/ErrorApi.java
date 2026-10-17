package api;

// Error que se le devuelve a la pagina web con su codigo HTTP (400, 404, 409...)
public class ErrorApi extends RuntimeException {

    private final int estado;

    public ErrorApi(int estado, String mensaje) {
        super(mensaje);
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }

}
