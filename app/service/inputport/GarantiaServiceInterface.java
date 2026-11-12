package service.inputport;

import domain.Garantia;

import java.time.LocalDate;
import java.util.List;

public interface GarantiaServiceInterface {

    public Garantia createGarantia(int idGarantia, int idPedido, LocalDate fechaInicio, int mesesCobertura, String descripcion);
    public Garantia selectGarantiaById(int id);
    public List<Garantia> selectAllGarantias();
    public Garantia updateGarantia(int idGarantia, int mesesCobertura, String descripcion);
    public void deleteGarantiaById(int id);
    public boolean estaVigente(int idGarantia);

}
