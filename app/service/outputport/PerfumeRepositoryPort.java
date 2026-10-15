package service.outputport;

import domain.Perfume;

import java.util.List;

public interface PerfumeRepositoryPort {

    public Perfume createPerfume(Perfume perfume);
    public Perfume selectPerfumeById(int id);
    public List<Perfume> selectAllPerfumes();
    public Perfume updatePerfume(Perfume perfume);
    public void deletePerfumeById(int id);

}
