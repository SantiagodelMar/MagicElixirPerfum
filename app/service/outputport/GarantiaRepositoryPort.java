package service.outputport;

import domain.Garantia;

import java.util.List;

public interface GarantiaRepositoryPort {

    public Garantia createGarantia(Garantia garantia);
    public Garantia selectGarantiaById(int id);
    public List<Garantia> selectAllGarantias();
    public Garantia updateGarantia(Garantia garantia);
    public void deleteGarantiaById(int id);

}
