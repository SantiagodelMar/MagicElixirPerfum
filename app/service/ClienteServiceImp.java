package service;

import domain.Cliente;
import domain.enums.TipoDocumento;
import service.inputport.ClienteServiceInterface;
import service.outputport.ClienteRepositoryPort;

import java.util.List;

public class ClienteServiceImp implements ClienteServiceInterface {

    private final ClienteRepositoryPort clienteRepository;

    public ClienteServiceImp(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // HU-03: registrar el cliente con telefono y direccion
    @Override
    public Cliente createCliente(int idCliente, String nombre, String apellido, String correo, TipoDocumento tipoDoc,
                                 String celular, String direccion, String fechaNacimiento, String tipoCliente, String direccionEnvio) {

        Cliente cliente = new Cliente(idCliente, nombre, apellido, correo, tipoDoc, celular, direccion,
                fechaNacimiento, tipoCliente, direccionEnvio);

        return clienteRepository.createCliente(cliente);
    }

    @Override
    public Cliente selectClienteById(int id) {
        return clienteRepository.selectClienteById(id);
    }

    @Override
    public List<Cliente> selectAllClientes() {
        return clienteRepository.selectAllClientes();
    }

    @Override
    public Cliente updateCliente(int idCliente, String celular, String direccion, String direccionEnvio) {
        Cliente cliente = clienteRepository.selectClienteById(idCliente);
        if (cliente == null) {
            return null;
        }

        cliente.setCelular(celular);
        cliente.setDireccion(direccion);
        cliente.setDireccionEnvio(direccionEnvio);

        return clienteRepository.updateCliente(cliente);
    }

    @Override
    public void deleteClienteById(int id) {
        clienteRepository.deleteClienteById(id);
    }

}
