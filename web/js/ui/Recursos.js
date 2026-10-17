/** Carga las imagenes de la carpeta img/ (las mismas de src/main/resources en Java). */
class Recursos {

    static imagen(nombre, ancho, alto) {
        const imagen = document.createElement("img");
        imagen.src = "img/" + nombre + ".png";
        imagen.alt = "";
        imagen.width = ancho;
        imagen.height = alto;
        return imagen;
    }
}
