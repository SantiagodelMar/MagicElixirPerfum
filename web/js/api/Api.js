/** Habla con el backend Java (app/api). Todas las llamadas devuelven JSON o lanzan un Error con el mensaje del servidor. */
class Api {

    // Si la pagina la entrega el propio Java (puerto 8080) se usa la misma direccion;
    // si se abre con Live Server o con doble clic, se apunta a localhost:8080
    static BASE = location.port === "8080" ? "" : "http://localhost:8080";

    static obtener(ruta) {
        return Api.#pedir("GET", ruta);
    }

    static enviar(ruta, datos) {
        return Api.#pedir("POST", ruta, datos);
    }

    static actualizar(ruta, datos) {
        return Api.#pedir("PUT", ruta, datos);
    }

    static async #pedir(metodo, ruta, datos) {
        let respuesta;
        try {
            respuesta = await fetch(Api.BASE + "/api" + ruta, {
                method: metodo,
                headers: datos ? { "Content-Type": "application/json" } : {},
                body: datos ? JSON.stringify(datos) : undefined,
            });
        } catch (e) {
            throw new ErrorConexion();
        }
        const json = await respuesta.json();
        if (!respuesta.ok) {
            const error = new Error(json.error || "Error del servidor");
            error.estado = respuesta.status;
            throw error;
        }
        return json;
    }
}

/** El servidor Java no esta encendido. */
class ErrorConexion extends Error {

    constructor() {
        super("No hay conexión con el servidor Java. Ejecuta Application.java en IntelliJ.");
    }
}
