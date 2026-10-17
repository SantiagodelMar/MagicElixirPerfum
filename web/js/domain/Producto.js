/** Clase base del catalogo. Perfume hereda de aqui; la tienda podria vender otros productos. */
class Producto {

    // ---- atributos ----
    #idProducto;
    #nombre;
    #precio;
    #stock;

    // ---- constructor ----
    constructor(idProducto, nombre, precio, stock) {
        if (new.target === Producto) {
            throw new Error("Producto es abstracta: usa una subclase como Perfume.");
        }
        this.#idProducto = idProducto;
        this.#nombre = nombre;
        this.#precio = precio;
        this.#stock = stock;
    }

    // ---- getters y setters ----
    getIdProducto() {
        return this.#idProducto;
    }

    getNombre() {
        return this.#nombre;
    }

    getPrecio() {
        return this.#precio;
    }

    getStock() {
        return this.#stock;
    }

    setStock(stock) {
        this.#stock = stock;
    }

    // ---- metodos de negocio ----
    hayStock(cantidad) {
        return cantidad > 0 && cantidad <= this.#stock;
    }

    precioFormateado() {
        return Dinero.pesos(this.#precio);
    }

    /** Iniciales para la foto generica de la tarjeta: "Khamrah Qahwa" -> "KQ". */
    iniciales() {
        return this.#nombre.split(/\s+/).filter((p) => /^[A-Za-zÁÉÍÓÚÑ]/.test(p))
            .slice(0, 2).map((p) => p[0].toUpperCase()).join("");
    }

    /** Cada subclase muestra sus propios datos (polimorfismo). */
    mostrarInfo() {
        throw new Error("Cada subclase debe implementar mostrarInfo()");
    }

    /** Nombre de la categoria con la que se filtra en el catalogo. */
    categoria() {
        throw new Error("Cada subclase debe implementar categoria()");
    }

    toString() {
        return this.#nombre + " - " + this.precioFormateado();
    }
}
