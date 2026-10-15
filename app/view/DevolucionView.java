package view;

import domain.Devolucion;
import domain.enums.MotivoDevolucion;
import service.helpers.SetDevolucionHelper;
import service.inputport.DevolucionServiceInterface;
import service.validations.FormTypeValidator;

import java.time.LocalDate;
import java.util.List;

public class DevolucionView {

    private final DevolucionServiceInterface devolucionService;

    public DevolucionView(DevolucionServiceInterface devolucionService) {
        this.devolucionService = devolucionService;
    }

    public void createDevolucion() {
        int idDevolucion = FormTypeValidator.intValidator("Ingrese el id de la devolucion");

        if (devolucionService.selectDevolucionById(idDevolucion) != null) {
            System.out.println("Ya existe una devolucion con ese id");
            return;
        }

        int idPedido = FormTypeValidator.intValidator("Ingrese el id del pedido");
        LocalDate fechaEntrega = FormTypeValidator.dateValidator("Ingrese la fecha de entrega del pedido (formato: yyyy-MM-dd)");
        MotivoDevolucion motivo = SetDevolucionHelper.getMotivo();
        String descripcion = FormTypeValidator.stringValidator("Ingrese una descripcion");
        double totalPagado = FormTypeValidator.doubleValidator("Ingrese el total pagado del pedido");

        Devolucion devolucion = devolucionService.createDevolucion(idDevolucion, idPedido, fechaEntrega,
                motivo, descripcion, totalPagado);

        System.out.println("Devolucion registrada con estado: " + devolucion.getEstado().getDescripcion());
        if (devolucion.getMontoReembolso() == 0) {
            System.out.println("La devolucion fue rechazada porque ya pasaron mas de 30 dias desde la entrega");
        } else {
            System.out.println("Monto a reembolsar: $" + devolucion.getMontoReembolso());
        }
    }

    public void selectDevolucionById() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la devolucion a buscar");
        Devolucion devolucion = devolucionService.selectDevolucionById(id);

        if (devolucion == null) {
            System.out.println("No se encontro la devolucion");
        } else {
            mostrarDevolucion(devolucion);
        }
    }

    public void selectAllDevoluciones() {
        List<Devolucion> devoluciones = devolucionService.selectAllDevoluciones();

        if (devoluciones.isEmpty()) {
            System.out.println("No hay devoluciones registradas");
            return;
        }

        for (Devolucion devolucion : devoluciones) {
            mostrarDevolucion(devolucion);
        }
    }

    public void updateDevolucion() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la devolucion a actualizar");

        if (devolucionService.selectDevolucionById(id) == null) {
            System.out.println("No se encontro la devolucion");
            return;
        }

        MotivoDevolucion motivo = SetDevolucionHelper.getMotivo();
        String descripcion = FormTypeValidator.stringValidator("Ingrese la nueva descripcion");

        devolucionService.updateDevolucion(id, motivo, descripcion);
        System.out.println("Devolucion actualizada");
    }

    public void deleteDevolucion() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la devolucion a eliminar");

        if (devolucionService.selectDevolucionById(id) == null) {
            System.out.println("No se encontro la devolucion");
            return;
        }

        devolucionService.deleteDevolucionById(id);
        System.out.println("Devolucion eliminada");
    }

    public void registrarReembolso() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la devolucion a reembolsar");
        Devolucion devolucion = devolucionService.registrarReembolso(id);

        if (devolucion == null) {
            System.out.println("Solo se pueden reembolsar devoluciones aprobadas");
        } else {
            System.out.println("Reembolso registrado por $" + devolucion.getMontoReembolso());
        }
    }

    private void mostrarDevolucion(Devolucion devolucion) {
        System.out.println("Devolucion #" + devolucion.getIdDevolucion()
                + " | Pedido: " + devolucion.getIdPedido()
                + " | Entrega: " + devolucion.getFechaEntrega()
                + " | Motivo: " + devolucion.getMotivo().getDescripcion()
                + " | Estado: " + devolucion.getEstado().getDescripcion()
                + " | Reembolso: $" + devolucion.getMontoReembolso());
    }

}//Fin public class
