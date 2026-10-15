package service.outputport;

import domain.Cliente;

import java.util.List;

public interface ClienteRepositoryPort {

    public Cliente createCliente(Cliente cliente);
    public Cliente selectClienteById(int id);
    public List<Cliente> selectAllClientes();
    public Cliente updateCliente(Cliente cliente);
    public void deleteClienteById(int id);

}
