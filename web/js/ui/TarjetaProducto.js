/** Tarjeta del catalogo: monograma, marca, nombre, notas, precio y boton de compra. */
class TarjetaProducto {

    elemento = PanelRedondo.crear("tarjeta");

    constructor(producto, ventana) {
        this.elemento.append(this.#foto(producto), this.#datos(producto), this.#compra(producto, ventana));
    }

    /** Los perfumes de Java no traen foto: se dibuja un monograma con las iniciales. */
    #foto(producto) {
        const marco = document.createElement("div");
        marco.className = "foto monograma";
        const iniciales = document.createElement("span");
        iniciales.textContent = producto.iniciales();
        marco.append(iniciales, Estilo.micro(producto.getMarca()));
        return marco;
    }

    #datos(producto) {
        const bloque = Caja.columna("datos");
        const notas = Estilo.texto(producto.getNotasOlfativas(), 11.5, "gris");
        notas.classList.add("descripcion");
        bloque.append(Estilo.micro(producto.categoria()), Caja.aire(7),
            Estilo.titulo(producto.getNombre(), 19), Caja.aire(6),
            notas, Caja.aire(9),
            Estilo.micro(producto.getMarca() + " · " + producto.getMililitros() + " ml"));
        return bloque;
    }

    #compra(producto, ventana) {
        const bloque = Caja.columna("compra");
        bloque.append(Estilo.titulo(producto.precioFormateado(), 22));
        const stock = producto.getStock();
        if (stock <= 3) {
            bloque.append(Caja.aire(4), Estilo.micro(stock === 0 ? "Agotado" : "Quedan " + stock + " unidades"));
        }
        bloque.append(Caja.aire(12));
        const agregar = new Boton(stock === 0 ? "Agotado" : "Agregar al carrito", true);
        agregar.setEnabled(stock > 0);
        agregar.alClic(() => {
            const agregado = ventana.getTienda().getCarrito().agregarProducto(producto, 1);
            ventana.actualizarCabecera();
            this.#confirmar(agregar, agregado ? "Agregado" : "Sin más unidades");
        });
        bloque.append(agregar.elemento);
        return bloque;
    }

    /** Pequeno aviso en el mismo boton para que se note que si quedo agregado. */
    #confirmar(boton, texto) {
        boton.setTexto(texto);
        setTimeout(() => boton.setTexto("Agregar al carrito"), 1200);
    }
}
