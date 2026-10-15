package service.helpers;

import domain.enums.EstadoInventario;

public class SetInventarioHelper {

    // HU-07: el estado depende de cuantas unidades quedan
    public static EstadoInventario calcularEstado(int stock) {
        if (stock == 0) {
            return EstadoInventario.AGOTADO;
        } else if (stock <= 5) {
            return EstadoInventario.BAJO_STOCK;
        } else {
            return EstadoInventario.DISPONIBLE;
        }
    }

}
