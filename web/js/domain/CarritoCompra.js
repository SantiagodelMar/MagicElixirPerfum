/** Carrito del cliente: agrega, elimina, cambia cantidades y calcula el total. Vive en el navegador hasta confirmar. */
class CarritoCompra {

    #listaProductos = [];

    getListaProductos() {
        return this.#listaProductos;
    }

    /** Devuelve false si ya no hay mas unidades disponibles en el inventario. */
    agregarProducto(producto, cantidad) {
        const item = this.#buscar(producto.getIdProducto());
        const enCarrito = item === null ? 0 : item.getCantidad();
        if (!producto.hayStock(enCarrito + cantidad)) {
            return false;
        }
        if (item !== null) {
            item.sumar(cantidad);
        } else {
            this.#listaProductos.push(new ItemCarrito(producto, cantidad));
        }
        return true;
    }

    eliminarProducto(idProducto) {
        const item = this.#buscar(idProducto);
        if (item !== null) {
            this.#quitar(item);
        }
    }

    /** Si la cantidad baja a cero la linea se quita del carrito. */
    actualizarCantidad(idProducto, cantidad) {
        const item = this.#buscar(idProducto);
        if (item === null) {
            return;
        }
        if (cantidad <= 0) {
            this.#quitar(item);
        } else {
            item.setCantidad(Math.min(cantidad, item.getProducto().getStock()));
        }
    }

    calcularTotal() {
        let total = 0;
        for (const item of this.#listaProductos) {
            total += item.subtotal();
        }
        return total;
    }

    cantidadArticulos() {
        let unidades = 0;
        for (const item of this.#listaProductos) {
            unidades += item.getCantidad();
        }
        return unidades;
    }

    estaVacio() {
        return this.#listaProductos.length === 0;
    }

    vaciar() {
        this.#listaProductos.length = 0;
    }

    /** Lo que espera POST /api/pedidos: [{ idPerfume, cantidad }]. */
    items() {
        return this.#listaProductos.map((item) => ({
            idPerfume: item.getProducto().getIdProducto(),
            cantidad: item.getCantidad(),
        }));
    }

    /** Tras recargar el catalogo, el carrito apunta a los productos nuevos (con su stock actualizado). */
    reemplazarProductos(productos) {
        for (const item of [...this.#listaProductos]) {
            const nuevo = productos.find((p) => p.getIdProducto() === item.getProducto().getIdProducto());
            this.#quitar(item);
            if (nuevo && nuevo.getStock() > 0) {
                this.#listaProductos.push(new ItemCarrito(nuevo, Math.min(item.getCantidad(), nuevo.getStock())));
            }
        }
    }

    #buscar(idProducto) {
        for (const item of this.#listaProductos) {
            if (item.getProducto().getIdProducto() === idProducto) {
                return item;
            }
        }
        return null;
    }

    #quitar(item) {
        this.#listaProductos.splice(this.#listaProductos.indexOf(item), 1);
    }
}
