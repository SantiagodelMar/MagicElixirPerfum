
class Dinero {

    static pesos(valor) {
        const entero = Math.round(valor).toString();
        return "$ " + entero.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    }
}
