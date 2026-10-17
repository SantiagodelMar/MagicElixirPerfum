/** Tipografias de la tienda: serif para vender, sans para leer. La paleta esta en css/estilos.css. */
class Estilo {

   
    static micro(texto) {
        const etiqueta = document.createElement("span");
        etiqueta.className = "micro";
        etiqueta.textContent = texto;
        return etiqueta;
    }

    static titulo(texto, tamano) {
        const etiqueta = document.createElement("div");
        etiqueta.className = "titulo";
        etiqueta.style.fontSize = tamano + "px";
        etiqueta.textContent = texto;
        return etiqueta;
    }

    
    static texto(contenido, tamano, color = "gris") {
        const etiqueta = document.createElement("div");
        etiqueta.className = "texto " + color;
        etiqueta.style.fontSize = tamano + "px";
        etiqueta.textContent = contenido;
        return etiqueta;
    }

   
    static campo(control) {
        control.classList.add("campo");
    }
}
