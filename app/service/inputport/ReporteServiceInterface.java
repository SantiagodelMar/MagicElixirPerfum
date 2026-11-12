package service.inputport;

import domain.ItemReporte;

import java.time.LocalDate;
import java.util.List;

public interface ReporteServiceInterface {

    public double totalVendido(LocalDate desde, LocalDate hasta);
    public List<ItemReporte> perfumesMasVendidos(LocalDate desde, LocalDate hasta, int limite);
    public List<ItemReporte> ventasPorCanal(LocalDate desde, LocalDate hasta);

}
