/** Una linea del carrito: el producto y cuantas unidades lleva el cliente. */
class ItemCarrito {

    #producto;
    #cantidad;

  
    constructor(producto, cantidad) {
        this.#producto = producto;
        this.#cantidad = cantidad;
    }

    
    getProducto() {
        return this.#producto;
    }

    getCantidad() {
        return this.#cantidad;
    }

    setCantidad(cantidad) {
        this.#cantidad = cantidad;
    }

    sumar(unidades) {
        this.#cantidad += unidades;
    }

    subtotal() {
        return this.#producto.getPrecio() * this.#cantidad;
    }

    subtotalFormateado() {
        return Dinero.pesos(this.subtotal());
    }
}
