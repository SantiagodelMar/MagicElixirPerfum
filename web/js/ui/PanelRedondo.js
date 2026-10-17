/** Caja blanca con esquinas redondeadas; se usa en tarjetas y en el resumen. */
class PanelRedondo {

    static crear(clase = "") {
        const panel = document.createElement("div");
        panel.className = ("panel-redondo " + clase).trim();
        return panel;
    }
}
