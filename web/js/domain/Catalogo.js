/** Catalogo que se carga desde el backend Java: perfumes con su stock y las categorias. */
class Catalogo {

    static async cargar() {
        const [perfumes, categorias] = await Promise.all([
            Api.obtener("/perfumes"),
            Api.obtener("/categorias"),
        ]);
        return {
            productos: perfumes.map((json) => Perfume.desdeJson(json)),
            categorias: ["Todos", ...categorias.map((c) => c.nombre)],
        };
    }

    /** Filtra por categoria y por el texto del buscador (nombre, marca o notas). */
    static filtrar(productos, texto, categoria) {
        const busqueda = texto == null ? "" : texto.trim().toLowerCase();
        return productos.filter((producto) => {
            const porCategoria = categoria == null || categoria === "Todos"
                || producto.categoria() === categoria;
            const porTexto = busqueda === ""
                || producto.getNombre().toLowerCase().includes(busqueda)
                || producto.getMarca().toLowerCase().includes(busqueda)
                || producto.mostrarInfo().toLowerCase().includes(busqueda);
            return porCategoria && porTexto;
        });
    }
}
