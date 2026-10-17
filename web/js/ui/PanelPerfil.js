/** Datos del cliente y sus pedidos. El estado de cada pedido lo cambia el empleado desde la consola. */
class PanelPerfil {

    #ventana;
    #nombre = Estilo.texto("", 14, "tinta");
    #correo = Estilo.texto("", 14, "tinta");
    #celular = Caja.entrada("tel", "tel");
    #direccion = Caja.entrada("text", "street-address");
    #aviso = Estilo.texto("", 12, "gris");
    #pedidos = Caja.columna("lista-pedidos");
    elemento = document.createElement("section");

    constructor(ventana) {
        this.#ventana = ventana;
        this.elemento.className = "vista centrado perfil";
        const datos = PanelRedondo.crear("perfil-caja");
        datos.append(this.#formulario());
        const pedidos = PanelRedondo.crear("perfil-caja");
        pedidos.append(this.#seccionPedidos());
        this.elemento.append(datos, pedidos);
    }

    #formulario() {
        const panel = document.createElement("form");
        panel.className = "columna";
        panel.noValidate = true;
        panel.addEventListener("submit", (e) => {
            e.preventDefault();
            this.#guardar();
        });

        const guardar = new Boton("Guardar cambios", true);
        guardar.elemento.type = "submit";
        const salir = new Boton("Cerrar sesión", false);
        salir.alClic(() => {
            this.#ventana.getTienda().cerrarSesion();
            this.#ventana.mostrar("acceso");
        });

        this.#aviso.classList.add("aviso-ok");
        panel.append(Estilo.micro("Mi cuenta"), Caja.aire(10),
            Estilo.titulo("Mis datos", 32), Caja.aire(24),
            Estilo.micro("Nombre"), Caja.aire(6), this.#nombre, Caja.aire(16),
            Estilo.micro("Correo"), Caja.aire(6), this.#correo, Caja.aire(20),
            Caja.campo("Celular", this.#celular),
            Caja.campo("Dirección de envío", this.#direccion),
            this.#aviso, Caja.aire(14),
            guardar.elemento, Caja.aire(10),
            salir.elemento);
        return panel;
    }

    #seccionPedidos() {
        const panel = Caja.columna();
        const actualizar = new Boton("Actualizar estados", false);
        actualizar.compacto();
        actualizar.alClic(() => this.#cargarPedidos());
        panel.append(Estilo.micro("Historial"), Caja.aire(10),
            Estilo.titulo("Mis pedidos", 32), Caja.aire(8),
            Estilo.texto("El estado lo actualiza la tienda desde el sistema (menú 7. Pedidos).", 13, "gris"),
            Caja.aire(16), this.#pedidos, Caja.aire(16), actualizar.elemento);
        return panel;
    }

    async #guardar() {
        try {
            await this.#ventana.getTienda().actualizarDatos(this.#celular.value.trim(), this.#direccion.value.trim());
            this.#aviso.textContent = "Datos actualizados.";
        } catch (error) {
            this.#aviso.textContent = error.message;
        }
    }

    async #cargarPedidos() {
        const pedidos = await this.#ventana.getTienda().misPedidos();
        this.#pedidos.replaceChildren();
        if (pedidos.length === 0) {
            this.#pedidos.append(Estilo.texto("Todavía no tienes pedidos.", 13, "gris"));
        }
        for (const pedido of pedidos) {
            const fila = document.createElement("div");
            fila.className = "fila-pedido";
            const izquierda = Caja.columna();
            izquierda.append(Estilo.titulo("Pedido " + pedido.getNumero(), 17), Caja.aire(4),
                Estilo.texto(pedido.fechaFormateada() + "  ·  " + pedido.cantidadArticulos() + " perfumes  ·  "
                    + Dinero.pesos(pedido.getTotal()), 12, "gris"));
            const estado = Estilo.micro(pedido.getEstado());
            estado.classList.add("estado");
            fila.append(izquierda, estado);
            this.#pedidos.append(fila);
        }
    }

    /** Carga los datos del cliente activo y sus pedidos. */
    async refrescar() {
        const cliente = this.#ventana.getTienda().getClienteActivo();
        if (cliente === null) {
            return;
        }
        this.#nombre.textContent = cliente.nombreCompleto();
        this.#correo.textContent = cliente.getCorreo();
        this.#celular.value = cliente.getCelular();
        this.#direccion.value = cliente.getDireccionEnvio();
        this.#aviso.textContent = "";
        await this.#cargarPedidos();
    }
}
