/** Vista del carrito: las lineas a la izquierda y el resumen a la derecha. */
class PanelCarrito {

    #ventana;
    #lineas = Caja.columna("lineas");
    #resumen;
    elemento = document.createElement("section");

    constructor(ventana) {
        this.#ventana = ventana;
        this.#resumen = new ResumenPedido(ventana);
        this.elemento.className = "vista carrito";

        const titulo = Caja.columna("carrito-titulo");
        titulo.append(Estilo.micro("Carrito de compras"), Caja.aire(8),
            Estilo.titulo("Tu selección", 34), Caja.aire(22));

        this.elemento.append(titulo, this.#lineas, this.#resumen.elemento);
    }

    /** Dibuja de nuevo las lineas y actualiza los totales en vivo. */
    refrescar() {
        const carrito = this.#ventana.getTienda().getCarrito();
        this.#lineas.replaceChildren();
        if (carrito.estaVacio()) {
            this.#lineas.append(Estilo.texto("Tu carrito está vacío por ahora.", 14, "gris"));
        }
        for (const item of carrito.getListaProductos()) {
            this.#lineas.append(new FilaCarrito(item, this.#ventana, () => this.refrescar()).elemento);
        }
        this.#resumen.actualizar();
        this.#ventana.actualizarCabecera();
    }
}
