/** Ventana unica de la aplicacion. Cambia de vista mostrando una seccion y ocultando las demas. */
class VentanaPrincipal {

    #tienda = new Tienda();
    #vistas;
    #avisoConexion = document.createElement("div");

    #cabecera;
    #panelCatalogo;
    #panelCarrito;
    #panelOrden;
    #panelPerfil;
    #vistaActual = "acceso";

    constructor(raiz) {
        document.title = "Magic Elixir Perfum  |  Tienda en línea";

        this.#cabecera = new Cabecera(this);
        this.#panelCatalogo = new PanelCatalogo(this);
        this.#panelCarrito = new PanelCarrito(this);
        this.#panelOrden = new PanelOrden(this);
        this.#panelPerfil = new PanelPerfil(this);

        this.#vistas = {
            acceso: new PanelAcceso(this).elemento,
            catalogo: this.#panelCatalogo.elemento,
            carrito: this.#panelCarrito.elemento,
            orden: this.#panelOrden.elemento,
            perfil: this.#panelPerfil.elemento,
        };

        this.#avisoConexion.className = "aviso-conexion";
        this.#avisoConexion.hidden = true;

        const contenido = document.createElement("main");
        contenido.append(...Object.values(this.#vistas));
        raiz.append(this.#avisoConexion, this.#cabecera.elemento, contenido);
        this.#cabecera.setVisible(false);
    }

    getTienda() {
        return this.#tienda;
    }

    async mostrar(vista) {
        // Sin cliente no se entra a la tienda
        if (vista !== "acceso" && this.#tienda.getClienteActivo() === null) {
            vista = "acceso";
        }
        try {
            if (vista === "catalogo") {
                await this.#panelCatalogo.refrescar();
            } else if (vista === "carrito") {
                this.#panelCarrito.refrescar();
            } else if (vista === "perfil") {
                await this.#panelPerfil.refrescar();
            }
            this.#avisoConexion.hidden = true;
        } catch (error) {
            this.#mostrarError(error, vista);
        }
        this.#cabecera.setVisible(vista !== "acceso");
        this.#cabecera.refrescar();
        for (const [nombre, seccion] of Object.entries(this.#vistas)) {
            seccion.hidden = nombre !== vista;
        }
        if (vista !== this.#vistaActual) {
            window.scrollTo(0, 0);
        }
        this.#vistaActual = vista;
    }

    mostrarPedido(pedido) {
        this.#panelOrden.mostrarPedido(pedido);
        this.mostrar("orden");
    }

    actualizarCabecera() {
        this.#cabecera.refrescar();
    }

    /** Si el servidor Java no responde, se avisa arriba con un boton para reintentar. */
    #mostrarError(error, vista) {
        this.#avisoConexion.replaceChildren();
        const reintentar = new Boton("Reintentar", false);
        reintentar.compacto();
        reintentar.alClic(() => this.mostrar(vista));
        this.#avisoConexion.append(Estilo.texto(error.message, 13, "tinta"), reintentar.elemento);
        this.#avisoConexion.hidden = false;
    }
}
