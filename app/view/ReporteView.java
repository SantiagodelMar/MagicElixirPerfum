package view;

import domain.ItemReporte;
import service.inputport.ReporteServiceInterface;
import service.validations.FormTypeValidator;

import java.time.LocalDate;
import java.util.List;

// Reportes del administrador (Epica 8)
public class ReporteView {

    private final ReporteServiceInterface reporteService;

    public ReporteView(ReporteServiceInterface reporteService) {
        this.reporteService = reporteService;
    }

    // HU-22
    public void totalVendido() {
        LocalDate desde = FormTypeValidator.dateValidator("Fecha inicial (formato: yyyy-MM-dd)");
        LocalDate hasta = FormTypeValidator.dateValidator("Fecha final (formato: yyyy-MM-dd)");

        if (hasta.isBefore(desde)) {
            System.out.println("La fecha final no puede ser antes de la inicial");
            return;
        }
        System.out.println("Total vendido entre " + desde + " y " + hasta + ": $" + reporteService.totalVendido(desde, hasta));
    }

    // HU-23
    public void perfumesMasVendidos() {
        LocalDate desde = FormTypeValidator.dateValidator("Fecha inicial (formato: yyyy-MM-dd)");
        LocalDate hasta = FormTypeValidator.dateValidator("Fecha final (formato: yyyy-MM-dd)");
        int limite = FormTypeValidator.intValidator("Cuantos perfumes quiere ver (ej: 5)");

        List<ItemReporte> lista = reporteService.perfumesMasVendidos(desde, hasta, limite);
        if (lista.isEmpty()) {
            System.out.println("No hubo ventas en ese rango");
            return;
        }
        int puesto = 1;
        for (ItemReporte item : lista) {
            System.out.println(puesto + ". " + item.getNombre() + " | Unidades: " + item.getCantidad() + " | Vendido: $" + item.getTotal());
            puesto++;
        }
    }

    // HU-24
    public void ventasPorCanal() {
        LocalDate desde = FormTypeValidator.dateValidator("Fecha inicial (formato: yyyy-MM-dd)");
        LocalDate hasta = FormTypeValidator.dateValidator("Fecha final (formato: yyyy-MM-dd)");

        List<ItemReporte> lista = reporteService.ventasPorCanal(desde, hasta);
        if (lista.isEmpty()) {
            System.out.println("No hubo ventas en ese rango");
            return;
        }
        for (ItemReporte item : lista) {
            System.out.println(item.getNombre() + " | Pedidos: " + item.getCantidad() + " | Vendido: $" + item.getTotal());
        }
    }

}//Fin public class
