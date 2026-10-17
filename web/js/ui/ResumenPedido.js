/** Caja de la derecha en el carrito: total, metodo de pago y confirmar. */
class ResumenPedido {

    #ventana;
    #articulos = Estilo.texto("", 13.5, "tinta");
    #total = Estilo.titulo("", 28);
    #metodoPago = document.createElement("select");
    #aviso = Caja.aviso();
    #confirmar = new Boton("Confirmar pedido", true);
    elemento = PanelRedondo.crear("resumen");

    constructor(ventana) {
        this.#ventana = ventana;

        this.#confirmar.alClic(() => this.#confirmarPedido());
        const seguir = new Boton("Seguir comprando", false);
        seguir.alClic(() => ventana.mostrar("catalogo"));

        const pago = Caja.campo("Método de pago", this.#metodoPago);

        const columna = Caja.columna();
        columna.append(Estilo.micro("Resumen del pedido"), Caja.aire(20),
            this.#renglon("Artículos", this.#articulos), Caja.aire(18),
            Estilo.micro("Total a pagar"), Caja.aire(4),
            this.#total, Caja.aire(22),
            pago,
            this.#aviso, Caja.aire(8),
            this.#confirmar.elemento, Caja.aire(10),
            seguir.elemento, Caja.aire(16),
            Estilo.micro("IVA incluido en el precio"), Caja.aire(6),
            Estilo.micro("El pedido queda pendiente de pago"));
        this.elemento.append(columna);
    }

    /** Vuelve a escribir los totales cada vez que cambia el carrito. */
    actualizar() {
        const tienda = this.#ventana.getTienda();
        const carrito = tienda.getCarrito();
        this.#articulos.textContent = String(carrito.cantidadArticulos());
        this.#total.textContent = Dinero.pesos(carrito.calcularTotal());
        this.#confirmar.setEnabled(!carrito.estaVacio());
        this.#aviso.textContent = "";

        if (this.#metodoPago.options.length === 0) {
            for (const metodo of tienda.getMetodosPago()) {
                this.#metodoPago.append(new Option(metodo.descripcion, metodo.codigo));
            }
        }
    }

    async #confirmarPedido() {
        const tienda = this.#ventana.getTienda();
        if (tienda.getCarrito().estaVacio()) {
            return;
        }
        this.#confirmar.setEnabled(false);
        try {
            const pedido = await tienda.confirmarCompra(this.#metodoPago.value);
            this.#ventana.mostrarPedido(pedido);
        } catch (error) {
            // Por ejemplo, alguien compro las ultimas unidades: se recarga el stock
            this.#aviso.textContent = error.message;
            this.#confirmar.setEnabled(true);
            if (error.estado === 409) {
                await tienda.cargarCatalogo();
                const mensaje = error.message;
                await this.#ventana.mostrar("carrito");
                this.#aviso.textContent = mensaje;
            }
        }
    }

    #renglon(etiqueta, valor) {
        const fila = document.createElement("div");
        fila.className = "renglon";
        fila.append(Estilo.texto(etiqueta, 13.5, "gris"), valor);
        return fila;
    }
}
