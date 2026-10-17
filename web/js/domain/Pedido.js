/** Pedido confirmado. Lo crea el backend Java (PedidoService) y aqui solo se lee. */
class Pedido {

    // ---- atributos ----
    #id;
    #fecha;
    #cliente;
    #direccionEnvio;
    #estado;
    #metodoPago;
    #detalles;
    #total;

    constructor(json) {
        this.#id = json.id;
        this.#fecha = json.fecha;
        this.#cliente = json.cliente;
        this.#direccionEnvio = json.direccionEnvio;
        this.#estado = json.estadoDescripcion;
        this.#metodoPago = json.metodoPagoDescripcion;
        this.#detalles = json.detalles;
        this.#total = json.total;
    }

    // ---- getters ----
    getNumero() {
        return "#" + this.#id;
    }

    getCliente() {
        return this.#cliente;
    }

    getDireccionEnvio() {
        return this.#direccionEnvio;
    }

    getEstado() {
        return this.#estado;
    }

    getMetodoPago() {
        return this.#metodoPago;
    }

    getDetalles() {
        return this.#detalles;
    }

    getTotal() {
        return this.#total;
    }

    /** Java manda la fecha como 2026-10-17; aqui se muestra 17/10/2026. */
    fechaFormateada() {
        const [anio, mes, dia] = this.#fecha.split("-");
        return dia + "/" + mes + "/" + anio;
    }

    cantidadArticulos() {
        return this.#detalles.reduce((suma, d) => suma + d.cantidad, 0);
    }
}
