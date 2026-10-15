package view;

import domain.Cliente;
import domain.enums.TipoDocumento;
import service.helpers.SetUsuarioHelper;
import service.inputport.ClienteServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class ClienteView {

    private final ClienteServiceInterface clienteService;

    public ClienteView(ClienteServiceInterface clienteService) {
        this.clienteService = clienteService;
    }

    public void createCliente() {
        int id = FormTypeValidator.intValidator("Ingrese el id del cliente");

        if (clienteService.selectClienteById(id) != null) {
            System.out.println("Ya existe un cliente con ese id");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nombre del cliente");
        String apellido = FormTypeValidator.stringValidator("Ingrese el apellido del cliente");
        String correo = FormTypeValidator.stringValidator("Ingrese el correo del cliente");
        TipoDocumento tipoDoc = SetUsuarioHelper.getTipoDocumento();
        String celular = FormTypeValidator.stringValidator("Ingrese el celular del cliente");
        String direccion = FormTypeValidator.stringValidator("Ingrese la direccion del cliente");
        String fechaNacimiento = FormTypeValidator.stringValidator("Ingrese la fecha de nacimiento (yyyy-MM-dd)");
        String tipoCliente = FormTypeValidator.stringValidator("Ingrese el tipo de cliente (Nuevo, Frecuente, Mayorista)");
        String direccionEnvio = FormTypeValidator.stringValidator("Ingrese la direccion de envio");

        clienteService.createCliente(id, nombre, apellido, correo, tipoDoc, celular, direccion,
                fechaNacimiento, tipoCliente, direccionEnvio);
        System.out.println("Cliente registrado");
    }

    public void selectClienteById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del cliente a buscar");
        Cliente cliente = clienteService.selectClienteById(id);

        if (cliente == null) {
            System.out.println("No se encontro el cliente");
        } else {
            mostrarCliente(cliente);
        }
    }

    public void selectAllClientes() {
        List<Cliente> clientes = clienteService.selectAllClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
            return;
        }

        for (Cliente cliente : clientes) {
            mostrarCliente(cliente);
        }
    }

    public void updateCliente() {
        int id = FormTypeValidator.intValidator("Ingrese el id del cliente a actualizar");

        if (clienteService.selectClienteById(id) == null) {
            System.out.println("No se encontro el cliente");
            return;
        }

        String celular = FormTypeValidator.stringValidator("Ingrese el nuevo celular");
        String direccion = FormTypeValidator.stringValidator("Ingrese la nueva direccion");
        String direccionEnvio = FormTypeValidator.stringValidator("Ingrese la nueva direccion de envio");

        clienteService.updateCliente(id, celular, direccion, direccionEnvio);
        System.out.println("Cliente actualizado");
    }

    public void deleteCliente() {
        int id = FormTypeValidator.intValidator("Ingrese el id del cliente a eliminar");

        if (clienteService.selectClienteById(id) == null) {
            System.out.println("No se encontro el cliente");
            return;
        }

        clienteService.deleteClienteById(id);
        System.out.println("Cliente eliminado");
    }

    private void mostrarCliente(Cliente cliente) {
        System.out.println("Cliente #" + cliente.getIdUsuario()
                + " | " + cliente.getNombre() + " " + cliente.getApellido()
                + " | " + cliente.getTipoDoc().getDescripcion()
                + " | Cel: " + cliente.getCelular()
                + " | Correo: " + cliente.getCorreo()
                + " | Envio: " + cliente.getDireccionEnvio()
                + " | Tipo: " + cliente.getTipoCliente());
    }

}//Fin public class
