package service;

import domain.Administrador;
import domain.enums.TipoDocumento;
import service.inputport.AdministradorServiceInterface;
import service.outputport.AdministradorRepositoryPort;

import java.util.List;

public class AdministradorServiceImp implements AdministradorServiceInterface {

    private final AdministradorRepositoryPort administradorRepository;

    public AdministradorServiceImp(AdministradorRepositoryPort administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public Administrador createAdministrador(int idAdministrador, String nombre, String apellido, String correo, TipoDocumento tipoDoc,
                                             String celular, String direccion, String fechaNacimiento, String permissions, String level) {

        Administrador administrador = new Administrador(idAdministrador, nombre, apellido, correo, tipoDoc, celular,
                direccion, fechaNacimiento, permissions, level);

        return administradorRepository.createAdministrador(administrador);
    }

    @Override
    public Administrador selectAdministradorById(int id) {
        return administradorRepository.selectAdministradorById(id);
    }

    @Override
    public List<Administrador> selectAllAdministradores() {
        return administradorRepository.selectAllAdministradores();
    }

    @Override
    public Administrador updateAdministrador(int idAdministrador, String permissions, String level) {
        Administrador administrador = administradorRepository.selectAdministradorById(idAdministrador);
        if (administrador == null) {
            return null;
        }

        administrador.setPermissions(permissions);
        administrador.setLevel(level);

        return administradorRepository.updateAdministrador(administrador);
    }

    @Override
    public void deleteAdministradorById(int id) {
        administradorRepository.deleteAdministradorById(id);
    }

}
