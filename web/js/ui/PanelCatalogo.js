/** Catalogo con buscador, filtros por categoria y la grilla de perfumes. */
class PanelCatalogo {

    #ventana;
    #busqueda = Caja.entrada("search");
    #filtros = Caja.fila(8);
    #conteo = Estilo.texto("", 13.5, "gris");
    #grilla = document.createElement("div");
    #chips = [];
    #categoria = "Todos";
    elemento = document.createElement("section");

    constructor(ventana) {
        this.#ventana = ventana;
        this.#grilla.className = "grilla";
        this.elemento.className = "vista";
        this.elemento.append(this.#encabezado(), this.#grilla);
    }

    #encabezado() {
        const panel = Caja.columna("catalogo-encabezado");
        panel.append(Estilo.micro("Colección disponible"), Caja.aire(8),
            Estilo.titulo("Elige el tuyo", 36), Caja.aire(6),
            this.#conteo, Caja.aire(16));

        this.#busqueda.addEventListener("input", () => this.#dibujar());
        const barra = Caja.campo("Buscar por nombre, marca o nota olfativa", this.#busqueda);
        barra.classList.add("buscador");
        panel.append(barra, this.#filtros);
        return panel;
    }

    /** Las categorias vienen del servidor, por eso los filtros se arman al refrescar. */
    #armarFiltros() {
        const categorias = this.#ventana.getTienda().categorias();
        if (this.#chips.length === categorias.length) {
            return;
        }
        this.#chips = [];
        this.#filtros.replaceChildren();
        for (const nombre of categorias) {
            const chip = new Boton(nombre, nombre === this.#categoria);
            chip.compacto();
            chip.alClic(() => this.#cambiarCategoria(nombre));
            this.#chips.push(chip);
            this.#filtros.append(chip.elemento);
        }
    }

    #cambiarCategoria(nombre) {
        this.#categoria = nombre;
        for (const chip of this.#chips) {
            chip.marcar(chip.getTexto() === nombre.toUpperCase());
        }
        this.#dibujar();
    }

    /** Pide al servidor el catalogo con el stock actual y dibuja las tarjetas. */
    async refrescar() {
        await this.#ventana.getTienda().cargarCatalogo();
        this.#armarFiltros();
        this.#dibujar();
    }

    #dibujar() {
        const tienda = this.#ventana.getTienda();
        const total = tienda.getProductos().length;
        this.#conteo.textContent = total + " perfumes originales, con el inventario en tiempo real.";
        this.#grilla.replaceChildren();
        const encontrados = tienda.buscar(this.#busqueda.value, this.#categoria);
        for (const producto of encontrados) {
            this.#grilla.append(new TarjetaProducto(producto, this.#ventana).elemento);
        }
        if (encontrados.length === 0) {
            this.#grilla.append(Estilo.texto("No hay perfumes con ese filtro.", 14, "gris"));
        }
    }
}
