package repository;

import domain.Garantia;
import service.outputport.GarantiaRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class GarantiaRepositoryImp implements GarantiaRepositoryPort {

    // Lista en memoria (version sin base de datos)
    private List<Garantia> garantias = new ArrayList<>();

    @Override
    public Garantia createGarantia(Garantia garantia) {
        garantias.add(garantia);
        return garantia;
    }

    @Override
    public Garantia selectGarantiaById(int id) {
        for (int i = 0; i < garantias.size(); i++) {
            if (garantias.get(i).getIdGarantia() == id) {
                return garantias.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Garantia> selectAllGarantias() {
        return garantias;
    }

    @Override
    public Garantia updateGarantia(Garantia garantia) {
        for (int i = 0; i < garantias.size(); i++) {
            if (garantias.get(i).getIdGarantia() == garantia.getIdGarantia()) {
                garantias.set(i, garantia);
                return garantia;
            }
        }
        return null;
    }

    @Override
    public void deleteGarantiaById(int id) {
        for (int i = 0; i < garantias.size(); i++) {
            if (garantias.get(i).getIdGarantia() == id) {
                garantias.remove(i);
                return;
            }
        }
    }

}
