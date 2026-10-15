package view;

import domain.DetallePedido;
import domain.Pedido;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;
import service.helpers.SetPedidoHelper;
import service.inputport.PedidoServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class PedidoView {

    private final PedidoServiceInterface pedidoService;

    public PedidoView(PedidoServiceInterface pedidoService) {
        this.pedidoService = pedidoService;
    }

    public void createPedido() {
        int id = FormTypeValidator.intValidator("Ingrese el id del pedido");

        if (pedidoService.selectPedidoById(id) != null) {
            System.out.println("Ya existe un pedido con ese id");
            return;
        }

        int idCliente = FormTypeValidator.intValidator("Ingrese el id del cliente");
        MetodoDePago metodoPago = SetPedidoHelper.getMetodoPago();
        String observaciones = FormTypeValidator.stringValidator("Ingrese las observaciones");

        Pedido pedido = pedidoService.createPedido(id, idCliente, metodoPago, observaciones);

        if (pedido == null) {
            System.out.println("No existe el cliente, primero registrelo");
            return;
        }

        System.out.println("Pedido creado para " + pedido.getCliente().getNombre());

        // HU-10: se pueden agregar varios perfumes
        int seguir = 1;
        while (seguir == 1) {
            agregarPerfume(id);
            seguir = FormTypeValidator.intValidator("¿Desea agregar otro perfume? 1. Si  2. No");
        }
    }

    public void agregarPerfumeAPedido() {
        int id = FormTypeValidator.intValidator("Ingrese el id del pedido");
        agregarPerfume(id);
    }

    private void agregarPerfume(int idPedido) {
        int idPerfume = FormTypeValidator.intValidator("Ingrese el id del perfume");
        int cantidad = FormTypeValidator.intValidator("Ingrese la cantidad");

        String mensaje = pedidoService.agregarPerfume(idPedido, idPerfume, cantidad);
        System.out.println(mensaje);
    }

    public void selectPedidoById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del pedido a buscar");
        Pedido pedido = pedidoService.selectPedidoById(id);

        if (pedido == null) {
            System.out.println("No se encontro el pedido");
        } else {
            mostrarPedido(pedido);
            for (DetallePedido detalle : pedido.getDetalles()) {
                System.out.println("   - " + detalle.getPerfume().getNombre()
                        + " x" + detalle.getCantidad()
                        + " | Unitario: $" + detalle.getPrecioUnitario()
                        + " | Subtotal: $" + detalle.getSubtotal());
            }
        }
    }

    public void selectAllPedidos() {
        List<Pedido> pedidos = pedidoService.selectAllPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados");
            return;
        }

        for (Pedido pedido : pedidos) {
            mostrarPedido(pedido);
        }
    }

    public void updatePedido() {
        int id = FormTypeValidator.intValidator("Ingrese el id del pedido a actualizar");

        if (pedidoService.selectPedidoById(id) == null) {
            System.out.println("No se encontro el pedido");
            return;
        }

        MetodoDePago metodoPago = SetPedidoHelper.getMetodoPago();
        String observaciones = FormTypeValidator.stringValidator("Ingrese las nuevas observaciones");

        pedidoService.updatePedido(id, metodoPago, observaciones);
        System.out.println("Pedido actualizado");
    }

    public void cambiarEstadoPedido() {
        int id = FormTypeValidator.intValidator("Ingrese el id del pedido");

        if (pedidoService.selectPedidoById(id) == null) {
            System.out.println("No se encontro el pedido");
            return;
        }

        EstadoPedido estado = SetPedidoHelper.getEstadoPedido();
        Pedido pedido = pedidoService.cambiarEstadoPedido(id, estado);
        System.out.println("El pedido quedo en estado: " + pedido.getEstadoPedido().getDescripcion());
    }

    public void deletePedido() {
        int id = FormTypeValidator.intValidator("Ingrese el id del pedido a eliminar");

        if (pedidoService.selectPedidoById(id) == null) {
            System.out.println("No se encontro el pedido");
            return;
        }

        pedidoService.deletePedidoById(id);
        System.out.println("Pedido eliminado");
    }

    private void mostrarPedido(Pedido pedido) {
        System.out.println("Pedido #" + pedido.getIdPedido()
                + " | Fecha: " + pedido.getFechaPedido()
                + " | Cliente: " + pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido()
                + " | Pago: " + pedido.getMetodoPago().getDescripcion()
                + " | Estado: " + pedido.getEstadoPedido().getDescripcion()
                + " | Total: $" + pedido.getTotalCostoPedido());
    }

}//Fin public class
