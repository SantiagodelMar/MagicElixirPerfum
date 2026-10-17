/** Atajos para armar filas y columnas sin repetir el mismo codigo de layout. */
class Caja {

    static columna(clase = "") {
        const panel = document.createElement("div");
        panel.className = ("columna " + clase).trim();
        return panel;
    }

    static fila(separacion) {
        const panel = document.createElement("div");
        panel.className = "fila";
        panel.style.gap = separacion + "px";
        return panel;
    }

    static aire(alto) {
        const espacio = document.createElement("div");
        espacio.className = "aire";
        espacio.style.height = alto + "px";
        return espacio;
    }

    static entrada(tipo = "text", autocompletar = "off") {
        const control = document.createElement("input");
        control.type = tipo;
        control.autocomplete = autocompletar;
        return control;
    }

    static aviso() {
        const aviso = document.createElement("div");
        aviso.className = "aviso";
        return aviso;
    }

    /** Rotulo pequeno arriba y campo de texto abajo, como en los formularios. */
    static campo(etiqueta, control) {
        const bloque = Caja.columna("campo-bloque");
        Estilo.campo(control);
        const rotulo = document.createElement("label");
        rotulo.append(Estilo.micro(etiqueta), Caja.aire(6), control);
        rotulo.className = "columna";
        bloque.append(rotulo, Caja.aire(18));
        return bloque;
    }

    /** Fila con un texto a la izquierda y su valor a la derecha. */
    static renglon(etiqueta, valor, tamano, claseValor = "tinta") {
        const fila = document.createElement("div");
        fila.className = "renglon";
        const derecha = Estilo.texto(valor, tamano, claseValor);
        fila.append(Estilo.texto(etiqueta, tamano, "gris"), derecha);
        return fila;
    }
}
