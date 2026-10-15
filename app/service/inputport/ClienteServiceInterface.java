package service.inputport;

import domain.Cliente;
import domain.enums.TipoDocumento;

import java.util.List;

public interface ClienteServiceInterface {

    public Cliente createCliente(int idCliente, String nombre, String apellido, String correo, TipoDocumento tipoDoc,
                                 String celular, String direccion, String fechaNacimiento, String tipoCliente, String direccionEnvio);
    public Cliente selectClienteById(int id);
    public List<Cliente> selectAllClientes();
    public Cliente updateCliente(int idCliente, String celular, String direccion, String direccionEnvio);
    public void deleteClienteById(int id);

}
