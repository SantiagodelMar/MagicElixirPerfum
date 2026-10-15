package repository;

import domain.Perfume;
import service.outputport.PerfumeRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class PerfumeRepositoryImp implements PerfumeRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Perfume> perfumes = new ArrayList<>();

    @Override
    public Perfume createPerfume(Perfume perfume) {
        perfumes.add(perfume);
        return perfume;
    }

    @Override
    public Perfume selectPerfumeById(int id) {
        for (int i = 0; i < perfumes.size(); i++) {
            if (perfumes.get(i).getIdPerfume() == id) {
                return perfumes.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Perfume> selectAllPerfumes() {
        return perfumes;
    }

    @Override
    public Perfume updatePerfume(Perfume perfume) {
        for (int i = 0; i < perfumes.size(); i++) {
            if (perfumes.get(i).getIdPerfume() == perfume.getIdPerfume()) {
                perfumes.set(i, perfume);
                return perfume;
            }
        }
        return null;
    }

    @Override
    public void deletePerfumeById(int id) {
        for (int i = 0; i < perfumes.size(); i++) {
            if (perfumes.get(i).getIdPerfume() == id) {
                perfumes.remove(i);
                return;
            }
        }
    }

}
