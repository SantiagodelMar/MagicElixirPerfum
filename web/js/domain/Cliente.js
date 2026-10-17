/** Cliente de la tienda. Los datos viven en el backend Java (domain/Cliente.java). */
class Cliente {

    // ---- atributos ----
    #id;
    #nombre;
    #apellido;
    #correo;
    #celular;
    #direccionEnvio;

    // ---- constructor ----
    constructor(id, nombre, apellido, correo, celular, direccionEnvio) {
        this.#id = id;
        this.#nombre = nombre;
        this.#apellido = apellido;
        this.#correo = correo;
        this.#celular = celular;
        this.#direccionEnvio = direccionEnvio;
    }

    static desdeJson(json) {
        return new Cliente(json.id, json.nombre, json.apellido, json.correo, json.celular, json.direccionEnvio);
    }

    // ---- getters ----
    getId() {
        return this.#id;
    }

    getNombre() {
        return this.#nombre;
    }

    getApellido() {
        return this.#apellido;
    }

    getCorreo() {
        return this.#correo;
    }

    getCelular() {
        return this.#celular;
    }

    getDireccionEnvio() {
        return this.#direccionEnvio;
    }

    // ---- metodos de negocio ----
    /** Valida en el navegador antes de enviar el registro al servidor. */
    registrar() {
        const campos = [this.#nombre, this.#apellido, this.#correo, this.#celular, this.#direccionEnvio];
        if (campos.some((c) => c == null || c.trim() === "")) {
            return false;
        }
        return this.#correo.includes("@") && this.#correo.includes(".");
    }

    /** Lo que espera POST /api/clientes. */
    aJson() {
        return {
            nombre: this.#nombre, apellido: this.#apellido, correo: this.#correo,
            celular: this.#celular, direccionEnvio: this.#direccionEnvio,
        };
    }

    nombreCompleto() {
        return this.#nombre + " " + this.#apellido;
    }

    primerNombre() {
        return this.#nombre.split(" ")[0];
    }
}
