package service.inputport;

import domain.Administrador;
import domain.enums.TipoDocumento;

import java.util.List;

public interface AdministradorServiceInterface {

    public Administrador createAdministrador(int idAdministrador, String nombre, String apellido, String correo, TipoDocumento tipoDoc,
                                             String celular, String direccion, String fechaNacimiento, String permissions, String level);
    public Administrador selectAdministradorById(int id);
    public List<Administrador> selectAllAdministradores();
    public Administrador updateAdministrador(int idAdministrador, String permissions, String level);
    public void deleteAdministradorById(int id);

}
