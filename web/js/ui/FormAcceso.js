/** Ingreso de clientes con su correo. El acceso se bloquea despues de tres intentos fallidos. */
class FormAcceso {

    #ventana;
    #correo = Caja.entrada("email", "email");
    #aviso = Caja.aviso();
    #ingresar = new Boton("Ingresar", true);
    #login;
    #registro;
    elemento = document.createElement("div");

    constructor(ventana) {
        this.#ventana = ventana;
        this.#login = this.#crearLogin();
        this.#registro = new FormRegistro(ventana, () => this.#mostrarForma("login")).elemento;

        const contenido = document.createElement("div");
        contenido.className = "forma";
        contenido.append(this.#login, this.#registro);
        this.elemento.className = "acceso-form";
        this.elemento.append(contenido);
        this.#mostrarForma("login");
    }

    #mostrarForma(nombre) {
        this.#login.hidden = nombre !== "login";
        this.#registro.hidden = nombre !== "registro";
    }

    #crearLogin() {
        const panel = document.createElement("form");
        panel.className = "columna";
        panel.noValidate = true;
        this.#ingresar.elemento.type = "submit";
        panel.addEventListener("submit", (e) => {
            e.preventDefault();
            this.#entrar();
        });

        panel.append(Estilo.micro("Cuenta de cliente"), Caja.aire(12),
            Estilo.titulo("Ingresa a tu cuenta", 32), Caja.aire(28),
            Caja.campo("Correo", this.#correo),
            this.#aviso, Caja.aire(14),
            this.#ingresar.elemento, Caja.aire(18),
            this.#enlace("Crear una cuenta nueva"), Caja.aire(30),
            Estilo.micro("Prueba:  anyi@gmail.com"));
        return panel;
    }

    async #entrar() {
        const tienda = this.#ventana.getTienda();
        if (tienda.bloqueado()) {
            return;
        }
        this.#ingresar.setEnabled(false);
        try {
            if (await tienda.iniciarSesion(this.#correo.value)) {
                this.#aviso.textContent = "";
                this.#correo.value = "";
                await this.#ventana.mostrar("catalogo");
            } else if (tienda.bloqueado()) {
                this.#aviso.textContent = "Tres intentos fallidos: acceso bloqueado.";
                return;
            } else {
                this.#aviso.textContent = "No hay ningún cliente con ese correo. Te quedan "
                    + tienda.intentosRestantes() + " intentos.";
            }
        } catch (error) {
            this.#aviso.textContent = error.message;
        }
        this.#ingresar.setEnabled(true);
    }

    #enlace(texto) {
        const boton = document.createElement("button");
        boton.type = "button";
        boton.className = "enlace gris";
        boton.textContent = texto;
        boton.addEventListener("click", () => this.#mostrarForma("registro"));
        return boton;
    }
}
