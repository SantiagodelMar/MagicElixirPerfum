/** Formulario para crear una cuenta nueva: crea el Cliente en el backend Java. */
class FormRegistro {

    #ventana;
    #nombre = Caja.entrada("text", "given-name");
    #apellido = Caja.entrada("text", "family-name");
    #correo = Caja.entrada("email", "email");
    #celular = Caja.entrada("tel", "tel");
    #direccion = Caja.entrada("text", "street-address");
    #aviso = Caja.aviso();
    #crear = new Boton("Crear cuenta", true);
    elemento = document.createElement("form");

    constructor(ventana, volverAlLogin) {
        this.#ventana = ventana;
        this.elemento.className = "columna";
        this.elemento.noValidate = true;
        this.elemento.addEventListener("submit", (e) => {
            e.preventDefault();
            this.#crearCuenta();
        });
        this.#crear.elemento.type = "submit";

        const nombres = document.createElement("div");
        nombres.className = "dos-columnas";
        nombres.append(Caja.campo("Nombre", this.#nombre), Caja.campo("Apellido", this.#apellido));

        this.elemento.append(Estilo.micro("Nuevo cliente"), Caja.aire(12),
            Estilo.titulo("Crea tu cuenta", 32), Caja.aire(24),
            nombres,
            Caja.campo("Correo", this.#correo),
            Caja.campo("Celular", this.#celular),
            Caja.campo("Dirección de envío", this.#direccion),
            this.#aviso, Caja.aire(12),
            this.#crear.elemento, Caja.aire(16),
            this.#volver(volverAlLogin));
    }

    async #crearCuenta() {
        const nuevo = new Cliente(null, this.#nombre.value.trim(), this.#apellido.value.trim(),
            this.#correo.value.trim(), this.#celular.value.trim(), this.#direccion.value.trim());
        this.#crear.setEnabled(false);
        const error = await this.#ventana.getTienda().registrar(nuevo);
        this.#crear.setEnabled(true);
        if (error === null) {
            this.#aviso.textContent = "";
            this.elemento.reset();
            await this.#ventana.mostrar("catalogo");
        } else {
            this.#aviso.textContent = error;
        }
    }

    #volver(accion) {
        const boton = document.createElement("button");
        boton.type = "button";
        boton.className = "enlace gris";
        boton.textContent = "Ya tengo cuenta, ingresar";
        boton.addEventListener("click", accion);
        return boton;
    }
}
