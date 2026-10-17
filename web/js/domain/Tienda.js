/** Centraliza catalogo, carrito y cliente activo. Los datos se guardan en el backend Java. */
class Tienda {

    // ---- atributos ----
    static MAX_INTENTOS = 3;

    #productos = [];
    #categorias = ["Todos"];
    #metodosPago = [];
    #carrito = new CarritoCompra();
    #clienteActivo = null;
    #intentosFallidos = 0;

    // ---- getters ----
    getProductos() {
        return this.#productos;
    }

    getCarrito() {
        return this.#carrito;
    }

    getClienteActivo() {
        return this.#clienteActivo;
    }

    getMetodosPago() {
        return this.#metodosPago;
    }

    categorias() {
        return this.#categorias;
    }

    intentosRestantes() {
        return Tienda.MAX_INTENTOS - this.#intentosFallidos;
    }

    bloqueado() {
        return this.#intentosFallidos >= Tienda.MAX_INTENTOS;
    }

    // ---- metodos de negocio ----
    /** Trae del servidor el catalogo con el stock actual. */
    async cargarCatalogo() {
        const catalogo = await Catalogo.cargar();
        this.#productos = catalogo.productos;
        this.#categorias = catalogo.categorias;
        this.#carrito.reemplazarProductos(this.#productos);
        if (this.#metodosPago.length === 0) {
            this.#metodosPago = await Api.obtener("/metodos-pago");
        }
    }

    /** El cliente ingresa con su correo (el Cliente de Java no tiene contraseña). */
    async iniciarSesion(correo) {
        try {
            const json = await Api.obtener("/clientes?correo=" + encodeURIComponent(correo.trim()));
            this.#clienteActivo = Cliente.desdeJson(json);
            this.#intentosFallidos = 0;
            return true;
        } catch (error) {
            if (error.estado !== 404) {
                throw error;
            }
            this.#intentosFallidos++;
            return false;
        }
    }

    /** Devuelve null si el registro fue correcto, o el mensaje del error. */
    async registrar(nuevo) {
        if (!nuevo.registrar()) {
            return "Revisa los datos: todos los campos son obligatorios y el correo debe ser válido.";
        }
        try {
            const json = await Api.enviar("/clientes", nuevo.aJson());
            this.#clienteActivo = Cliente.desdeJson(json);
            this.#intentosFallidos = 0;
            return null;
        } catch (error) {
            return error.message;
        }
    }

    async actualizarDatos(celular, direccionEnvio) {
        const json = await Api.actualizar("/clientes/" + this.#clienteActivo.getId(), { celular, direccionEnvio });
        this.#clienteActivo = Cliente.desdeJson(json);
    }

    async misPedidos() {
        const lista = await Api.obtener("/clientes/" + this.#clienteActivo.getId() + "/pedidos");
        return lista.map((json) => new Pedido(json)).reverse();
    }

    cerrarSesion() {
        this.#clienteActivo = null;
        this.#carrito.vaciar();
    }

    buscar(texto, categoria) {
        return Catalogo.filtrar(this.#productos, texto, categoria);
    }

    /** El carrito se convierte en un Pedido real en Java, que descuenta el inventario. */
    async confirmarCompra(metodoPago) {
        const json = await Api.enviar("/pedidos", {
            idCliente: this.#clienteActivo.getId(),
            metodoPago: metodoPago,
            items: this.#carrito.items(),
        });
        this.#carrito.vaciar();
        await this.cargarCatalogo();
        return new Pedido(json);
    }
}
