package repository;

import domain.Cliente;
import service.outputport.ClienteRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryImp implements ClienteRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Cliente> clientes = new ArrayList<>();

    @Override
    public Cliente createCliente(Cliente cliente) {
        clientes.add(cliente);
        return cliente;
    }

    @Override
    public Cliente selectClienteById(int id) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdUsuario() == id) {
                return clientes.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Cliente> selectAllClientes() {
        return clientes;
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdUsuario() == cliente.getIdUsuario()) {
                clientes.set(i, cliente);
                return cliente;
            }
        }
        return null;
    }

    @Override
    public void deleteClienteById(int id) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdUsuario() == id) {
                clientes.remove(i);
                return;
            }
        }
    }

}
