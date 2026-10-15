package repository;

import domain.Administrador;
import service.outputport.AdministradorRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class AdministradorRepositoryImp implements AdministradorRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Administrador> administradores = new ArrayList<>();

    @Override
    public Administrador createAdministrador(Administrador administrador) {
        administradores.add(administrador);
        return administrador;
    }

    @Override
    public Administrador selectAdministradorById(int id) {
        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getIdUsuario() == id) {
                return administradores.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Administrador> selectAllAdministradores() {
        return administradores;
    }

    @Override
    public Administrador updateAdministrador(Administrador administrador) {
        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getIdUsuario() == administrador.getIdUsuario()) {
                administradores.set(i, administrador);
                return administrador;
            }
        }
        return null;
    }

    @Override
    public void deleteAdministradorById(int id) {
        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getIdUsuario() == id) {
                administradores.remove(i);
                return;
            }
        }
    }

}
