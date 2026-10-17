/** Boton plano. Solido (negro) para la accion principal, con borde para las demas. */
class Boton {

    elemento = document.createElement("button");

    constructor(texto, solido) {
        this.elemento.type = "button";
        this.elemento.className = "boton";
        this.setTexto(texto);
        this.marcar(solido);
    }

    setTexto(texto) {
        this.elemento.textContent = texto.toUpperCase();
    }

    getTexto() {
        return this.elemento.textContent;
    }

    marcar(activo) {
        this.elemento.classList.toggle("solido", activo);
    }

    compacto() {
        this.elemento.classList.add("compacto");
    }

    setEnabled(activo) {
        this.elemento.disabled = !activo;
    }

    alClic(accion) {
        this.elemento.addEventListener("click", accion);
    }
}
