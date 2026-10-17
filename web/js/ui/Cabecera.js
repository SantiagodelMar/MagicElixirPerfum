/** Barra superior: marca a la izquierda, navegacion y carrito a la derecha. */
class Cabecera {

    #ventana;
    #carrito = new Boton("Carrito  ·  0", true);
    #saludo = Estilo.micro("");
    elemento = document.createElement("header");

    constructor(ventana) {
        this.#ventana = ventana;
        this.elemento.className = "cabecera";
        this.elemento.append(this.#marca(), this.#acciones());
    }

    #marca() {
        const bloque = Caja.fila(12);
        bloque.classList.add("marca");
        const textos = Caja.columna();
        textos.append(Estilo.titulo("Magic Elixir", 20), Estilo.micro("Perfum  ·  Tienda en línea"));
        bloque.append(Recursos.imagen("logo", 38, 38), textos);
        bloque.addEventListener("click", () => this.#ventana.mostrar("catalogo"));
        return bloque;
    }

    #acciones() {
        const bloque = Caja.fila(18);
        this.#carrito.compacto();
        this.#carrito.alClic(() => this.#ventana.mostrar("carrito"));
        bloque.append(this.#saludo, this.#enlace("Catálogo", "catalogo"),
            this.#enlace("Mi perfil", "perfil"), this.#carrito.elemento);
        return bloque;
    }

    #enlace(texto, vista) {
        const boton = document.createElement("button");
        boton.type = "button";
        boton.className = "enlace";
        boton.textContent = texto;
        boton.addEventListener("click", () => this.#ventana.mostrar(vista));
        return boton;
    }

    /** Actualiza el contador del carrito y el nombre de quien esta comprando. */
    refrescar() {
        const tienda = this.#ventana.getTienda();
        this.#carrito.setTexto("Carrito  ·  " + tienda.getCarrito().cantidadArticulos());
        if (tienda.getClienteActivo() !== null) {
            this.#saludo.textContent = "Hola, " + tienda.getClienteActivo().primerNombre();
        }
    }

    setVisible(visible) {
        this.elemento.hidden = !visible;
    }
}
