/** Perfume del catalogo. Los datos llegan del backend Java (domain/Perfume.java + inventario). */
class Perfume extends Producto {

    // ---- atributos ----
    #marca;
    #mililitros;
    #notasOlfativas;
    #categoria;

    // ---- constructor ----
    constructor(id, nombre, marca, precio, stock, mililitros, notasOlfativas, categoria) {
        super(id, nombre, precio, stock);
        this.#marca = marca;
        this.#mililitros = mililitros;
        this.#notasOlfativas = notasOlfativas;
        this.#categoria = categoria;
    }

    /** Crea el perfume a partir del JSON de GET /api/perfumes. */
    static desdeJson(json) {
        return new Perfume(json.id, json.nombre, json.marca, json.precio, json.stock,
            json.mililitros, json.notasOlfativas, json.categoria ? json.categoria.nombre : "");
    }

    // ---- getters ----
    getMarca() {
        return this.#marca;
    }

    getMililitros() {
        return this.#mililitros;
    }

    getNotasOlfativas() {
        return this.#notasOlfativas;
    }

    // ---- metodos de negocio ----
    mostrarInfo() {
        return this.#mililitros + " ml · " + this.#notasOlfativas;
    }

    categoria() {
        return this.#categoria;
    }
}
