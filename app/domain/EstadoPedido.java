package domain;

public enum EstadoPedido {

    // 1. LAS CONSTANTES O VALORES PERMITIDOS
    PENDIENTE("Pedido creado, pendiente de pago"),
    PAGADO("Pago confirmado exitosamente, pendiente de envío"),
    ENVIADO("En camino con el mensajero"),
    ENTREGADO("Entregado al cliente"),
    CANCELADO("Pedido cancelado");

    // 2. ATRIBUTO PRIVADO E INMUTABLE
    private final String descripcion;

    // 3. CONSTRUCTOR INTERNO DEL ENUM
    EstadoPedido(String descripcion){
        this.descripcion = descripcion;
    }
    // 4. MÉTOD GETTER (Para consultar el texto desde afuera)
    public String getDescripcion() {
        return descripcion;
    }
}//Fin public enum
