package view;

import domain.Envio;
import domain.enums.EstadoEnvio;
import service.helpers.SetEnvioHelper;
import service.inputport.EnvioServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class EnvioView {

    private final EnvioServiceInterface envioService;

    public EnvioView(EnvioServiceInterface envioService) {
        this.envioService = envioService;
    }

    public void createEnvio() {
        int id = FormTypeValidator.intValidator("Ingrese el id del envio");

        if (envioService.selectEnvioById(id) != null) {
            System.out.println("Ya existe un envio con ese id");
            return;
        }

        int idPedido = FormTypeValidator.intValidator("Ingrese el id del pedido");
        String transportadora = FormTypeValidator.stringValidator("Ingrese la transportadora (Servientrega, Interrapidisimo...)");
        String guia = FormTypeValidator.stringValidator("Ingrese el numero de guia");
        String direccion = FormTypeValidator.stringValidator("Ingrese la direccion de destino");
        double costo = FormTypeValidator.doubleValidator("Ingrese el costo del envio");

        Envio envio = envioService.createEnvio(id, idPedido, transportadora, guia, direccion, costo);

        if (envio == null) {
            System.out.println("No existe el pedido, el envio no se registro");
        } else {
            System.out.println("Envio registrado. Guia " + envio.getNumeroDeGuia() + " con " + envio.getTransportadora());
        }
    }

    public void selectEnvioById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del envio a buscar");
        Envio envio = envioService.selectEnvioById(id);

        if (envio == null) {
            System.out.println("No se encontro el envio");
        } else {
            mostrarEnvio(envio);
        }
    }

    // Para responder al cliente con el numero de guia
    public void rastrearEnvio() {
        String guia = FormTypeValidator.stringValidator("Ingrese el numero de guia");
        Envio envio = envioService.selectEnvioByGuia(guia);

        if (envio == null) {
            System.out.println("No se encontro un envio con esa guia");
        } else {
            mostrarEnvio(envio);
        }
    }

    public void selectAllEnvios() {
        List<Envio> envios = envioService.selectAllEnvios();

        if (envios.isEmpty()) {
            System.out.println("No hay envios registrados");
            return;
        }

        for (Envio envio : envios) {
            mostrarEnvio(envio);
        }
    }

    public void updateEnvio() {
        int id = FormTypeValidator.intValidator("Ingrese el id del envio a actualizar");

        if (envioService.selectEnvioById(id) == null) {
            System.out.println("No se encontro el envio");
            return;
        }

        String transportadora = FormTypeValidator.stringValidator("Ingrese la transportadora");
        String guia = FormTypeValidator.stringValidator("Ingrese el numero de guia");
        String direccion = FormTypeValidator.stringValidator("Ingrese la direccion de destino");
        double costo = FormTypeValidator.doubleValidator("Ingrese el costo del envio");

        envioService.updateEnvio(id, transportadora, guia, direccion, costo);
        System.out.println("Envio actualizado");
    }

    // HU-14
    public void actualizarEstadoEnvio() {
        int id = FormTypeValidator.intValidator("Ingrese el id del envio");

        if (envioService.selectEnvioById(id) == null) {
            System.out.println("No se encontro el envio");
            return;
        }

        EstadoEnvio estado = SetEnvioHelper.getEstadoEnvio();
        Envio envio = envioService.actualizarEstadoEnvio(id, estado);
        System.out.println("El envio quedo en estado: " + envio.getEstadoEnvio().getDescripcion());
    }

    public void deleteEnvio() {
        int id = FormTypeValidator.intValidator("Ingrese el id del envio a eliminar");

        if (envioService.selectEnvioById(id) == null) {
            System.out.println("No se encontro el envio");
            return;
        }

        envioService.deleteEnvioById(id);
        System.out.println("Envio eliminado");
    }

    private void mostrarEnvio(Envio envio) {
        String entrega = (envio.getFechaEntrega() == null) ? "Pendiente" : envio.getFechaEntrega().toString();

        System.out.println("Envio #" + envio.getIdEnvio()
                + " | Pedido: " + envio.getPedido().getIdPedido()
                + " | " + envio.getTransportadora()
                + " | Guia: " + envio.getNumeroDeGuia()
                + " | Destino: " + envio.getDireccionDestino()
                + " | Enviado: " + envio.getFechaEnvio()
                + " | Entrega: " + entrega
                + " | Estado: " + envio.getEstadoEnvio().getDescripcion()
                + " | Costo: $" + envio.getCostoEnvio());
    }

}//Fin public class
