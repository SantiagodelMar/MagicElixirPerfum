package service.outputport;

import domain.Administrador;

import java.util.List;

public interface AdministradorRepositoryPort {

    public Administrador createAdministrador(Administrador administrador);
    public Administrador selectAdministradorById(int id);
    public List<Administrador> selectAllAdministradores();
    public Administrador updateAdministrador(Administrador administrador);
    public void deleteAdministradorById(int id);

}
