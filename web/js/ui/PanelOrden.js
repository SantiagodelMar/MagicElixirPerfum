/** Resumen del pedido que acaba de crear el backend Java. */
class PanelOrden {

    #ventana;
    #contenido = Caja.columna();
    elemento = document.createElement("section");

    constructor(ventana) {
        this.#ventana = ventana;
        this.elemento.className = "vista centrado";
        const caja = PanelRedondo.crear("orden-caja");
        caja.append(this.#contenido);
        this.elemento.append(caja);
    }

    /** Arma el detalle con los datos del pedido recien creado. */
    mostrarPedido(pedido) {
        const c = this.#contenido;
        c.replaceChildren();
        c.append(Estilo.micro("Pedido " + pedido.getNumero()), Caja.aire(10),
            Estilo.titulo("Gracias por tu compra", 34), Caja.aire(8),
            Estilo.texto("Tu pedido ya está registrado en el sistema de Magic Elixir.", 13.5, "gris"),
            Caja.aire(24),
            this.#renglon("Fecha", pedido.fechaFormateada()),
            this.#renglon("Cliente", pedido.getCliente()),
            this.#renglon("Envío a", pedido.getDireccionEnvio()),
            this.#renglon("Método de pago", pedido.getMetodoPago()),
            this.#renglon("Estado", pedido.getEstado()),
            Caja.aire(18), Estilo.micro("Perfumes"), Caja.aire(10));

        for (const detalle of pedido.getDetalles()) {
            c.append(this.#renglon(detalle.cantidad + " x " + detalle.nombre, Dinero.pesos(detalle.subtotal)));
        }

        const seguir = new Boton("Volver al catálogo", true);
        seguir.alClic(() => this.#ventana.mostrar("catalogo"));
        const perfil = new Boton("Ver mis pedidos", false);
        perfil.alClic(() => this.#ventana.mostrar("perfil"));

        c.append(Caja.aire(14), Estilo.micro("Total"), Caja.aire(4),
            Estilo.titulo(Dinero.pesos(pedido.getTotal()), 30), Caja.aire(26),
            seguir.elemento, Caja.aire(10), perfil.elemento);
    }

    #renglon(etiqueta, valor) {
        const fila = Caja.renglon(etiqueta, valor, 13);
        fila.classList.add("con-aire");
        return fila;
    }
}
