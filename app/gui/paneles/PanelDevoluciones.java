package gui.paneles;

import domain.Devolucion;
import domain.Envio;
import domain.Pedido;
import domain.enums.EstadoDevolucion;
import domain.enums.MotivoDevolucion;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Combo;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.estilo.Formato;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.DevolucionServiceInterface;
import service.inputport.EnvioServiceInterface;
import service.inputport.PedidoServiceInterface;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.util.Arrays;

// Modulo de Post-Venta y Garantias (Santiago)
// HU-17: el cliente puede devolver hasta 30 dias despues de la entrega.
// Si es por arrepentimiento se le devuelve el 80%, en los demas casos el 100%.
// Las reglas estan en DevolucionServiceImp, aqui solo se piden los datos y se muestran.
public class PanelDevoluciones extends PanelModulo {

    private final DevolucionServiceInterface devolucionService;
    private final PedidoServiceInterface pedidoService;
    private final EnvioServiceInterface envioService;

    private final CampoTexto txtId = new CampoTexto("Ej: 2");
    private final CampoTexto txtIdPedido = new CampoTexto("Ej: 3");
    private final CampoTexto txtFechaEntrega = new CampoTexto("aaaa-mm-dd");
    private final Combo<MotivoDevolucion> cbMotivo = new Combo<>(motivo -> motivo.getDescripcion());
    private final CampoTexto txtDescripcion = new CampoTexto("¿Qué pasó con el producto?");
    private final CampoTexto txtTotalPagado = new CampoTexto("Ej: 260000");

    private final Boton btnGuardar = new Boton("Registrar devolución", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnReembolso = new Boton("Registrar reembolso", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Solicitud de devolución", 4);
    private final TarjetaTabla listado = new TarjetaTabla("Devoluciones", "Buscar pedido, motivo o estado...",
            new String[]{"ID", "Pedido", "Entrega", "Solicitud", "Motivo", "Pagado", "Reembolso", "Estado"});

    private int idSeleccionado = -1;

    public PanelDevoluciones(VentanaPrincipal ventana, DevolucionServiceInterface devolucionService,
                             PedidoServiceInterface pedidoService, EnvioServiceInterface envioService) {
        super(ventana, "Post-venta y garantías  ·  HU-17 y HU-19", "Devoluciones",
                "Hasta 30 días después de la entrega. Por arrepentimiento se reembolsa el 80%, por daño o error el 100%.");
        this.devolucionService = devolucionService;
        this.pedidoService = pedidoService;
        this.envioService = envioService;

        cbMotivo.cargar(Arrays.asList(MotivoDevolucion.values()));

        formulario.agregarCampo("ID devolución", txtId);
        formulario.agregarCampo("ID del pedido", txtIdPedido);
        formulario.agregarCampo("Fecha de entrega", txtFechaEntrega);
        formulario.agregarCampo("Total pagado", txtTotalPagado);
        formulario.agregarCampo("Motivo", cbMotivo);
        formulario.agregarCampo("Descripción", txtDescripcion, 3);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnReembolso);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnReembolso.addActionListener(e -> registrarReembolso());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Al salir del campo del pedido se traen el total y la fecha de entrega
        txtIdPedido.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                autocompletarDesdePedido();
            }
        });
        txtIdPedido.addActionListener(e -> autocompletarDesdePedido());

        Tabla tabla = listado.getTabla();
        tabla.anchos(45, 70, 95, 95, 190, 110, 110, 115);
        tabla.columnaEstado(7);
        tabla.alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
    }

    @Override
    public void refrescar() {
        cargarTabla();
        limpiarFormulario();
    }

    private void cargarTabla() {
        Tabla tabla = listado.getTabla();
        tabla.limpiar();
        for (Devolucion devolucion : devolucionService.selectAllDevoluciones()) {
            tabla.agregarFila(
                    devolucion.getIdDevolucion(),
                    "#" + devolucion.getIdPedido(),
                    devolucion.getFechaEntrega(),
                    devolucion.getFechaSolicitud(),
                    devolucion.getMotivo().getDescripcion(),
                    devolucion.getTotalPagado(),
                    devolucion.getMontoReembolso(),
                    devolucion.getEstado().getDescripcion());
        }
        listado.actualizarConteo();
    }

    // Si el pedido existe en el sistema se llenan solos el total y la fecha de entrega
    private void autocompletarDesdePedido() {
        if (idSeleccionado != -1) {
            return;
        }
        int idPedido;
        try {
            idPedido = Integer.parseInt(txtIdPedido.getTexto());
        } catch (NumberFormatException e) {
            return;
        }

        Pedido pedido = pedidoService.selectPedidoById(idPedido);
        if (pedido == null) {
            return;
        }
        txtTotalPagado.setText(String.valueOf((long) pedido.getTotalCostoPedido()));
        txtTotalPagado.limpiarError();

        for (Envio envio : envioService.selectAllEnvios()) {
            if (envio.getPedido().getIdPedido() == idPedido && envio.getFechaEntrega() != null) {
                txtFechaEntrega.setText(envio.getFechaEntrega().toString());
                txtFechaEntrega.limpiarError();
            }
        }
        formulario.setNota("Datos traídos del pedido #" + idPedido + " de "
                + pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido() + ".");
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID devolución");
            if (devolucionService.selectDevolucionById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe una devolución con el ID " + id);
                return;
            }
            int idPedido = ValidadorCampos.entero(txtIdPedido, "ID del pedido");
            LocalDate fechaEntrega = ValidadorCampos.fecha(txtFechaEntrega, "La fecha de entrega");
            if (fechaEntrega.isAfter(LocalDate.now())) {
                txtFechaEntrega.marcarError();
                mostrarError("La fecha de entrega no puede ser después de hoy");
                return;
            }
            MotivoDevolucion motivo = ValidadorCampos.seleccion(cbMotivo, "el motivo");
            String descripcion = ValidadorCampos.texto(txtDescripcion, "Descripción");
            double totalPagado = ValidadorCampos.dinero(txtTotalPagado, "El total pagado");

            Devolucion devolucion = devolucionService.createDevolucion(id, idPedido, fechaEntrega, motivo,
                    descripcion, totalPagado);

            // El service decide si se aprueba o se rechaza segun los dias
            if (devolucion.getEstado() == EstadoDevolucion.APROBADA) {
                mostrarExito("Devolución aprobada: se reembolsan " + Formato.dinero(devolucion.getMontoReembolso()));
            } else {
                mostrarError("Devolución rechazada: pasaron más de 30 días desde la entrega");
            }
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // Se puede corregir el motivo y la descripcion (si cambia el motivo el service recalcula el reembolso)
    private void actualizar() {
        try {
            MotivoDevolucion motivo = ValidadorCampos.seleccion(cbMotivo, "el motivo");
            String descripcion = ValidadorCampos.texto(txtDescripcion, "Descripción");

            Devolucion devolucion = devolucionService.updateDevolucion(idSeleccionado, motivo, descripcion);

            mostrarExito("Devolución actualizada. Reembolso: " + Formato.dinero(devolucion.getMontoReembolso()));
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void registrarReembolso() {
        Devolucion devolucion = devolucionService.registrarReembolso(idSeleccionado);
        if (devolucion == null) {
            mostrarError("Solo se pueden reembolsar devoluciones aprobadas");
            return;
        }
        mostrarExito("Reembolso de " + Formato.dinero(devolucion.getMontoReembolso()) + " registrado");
        int id = idSeleccionado;
        cargarTabla();
        listado.getTabla().seleccionarId(id);
    }

    private void eliminar() {
        boolean confirmado = confirmar("¿Eliminar devolución?",
                "Se borrará la devolución #" + idSeleccionado + " del pedido #" + txtIdPedido.getTexto() + ".",
                "Eliminar");
        if (confirmado) {
            devolucionService.deleteDevolucionById(idSeleccionado);
            mostrarExito("Devolución eliminada");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Devolucion devolucion = devolucionService.selectDevolucionById(id);
        if (devolucion == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(devolucion.getIdDevolucion()));
        txtIdPedido.setText(String.valueOf(devolucion.getIdPedido()));
        txtFechaEntrega.setText(devolucion.getFechaEntrega().toString());
        cbMotivo.setSelectedItem(devolucion.getMotivo());
        txtDescripcion.setText(devolucion.getDescripcion());
        txtTotalPagado.setText(String.valueOf((long) devolucion.getTotalPagado()));

        quitarErrores(txtId, txtIdPedido, txtFechaEntrega, txtDescripcion, txtTotalPagado);
        modoEdicion(true);

        // El reembolso solo se puede registrar si esta aprobada
        btnReembolso.setEnabled(devolucion.getEstado() == EstadoDevolucion.APROBADA);
        formulario.setNota("Estado: " + devolucion.getEstado().getDescripcion()
                + "  ·  Reembolso: " + Formato.dinero(devolucion.getMontoReembolso()));
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtIdPedido.limpiar();
        txtFechaEntrega.limpiar();
        txtDescripcion.limpiar();
        txtTotalPagado.limpiar();
        cbMotivo.setSelectedIndex(0);

        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        txtIdPedido.setBloqueado(editando);
        txtFechaEntrega.setBloqueado(editando);
        txtTotalPagado.setBloqueado(editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnReembolso.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
        } else {
            formulario.setModo(false, "Nueva solicitud");
            formulario.setNota("Escribe el ID del pedido y se llenan el total y la fecha.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (Devolucion devolucion : devolucionService.selectAllDevoluciones()) {
            mayor = Math.max(mayor, devolucion.getIdDevolucion());
        }
        return mayor + 1;
    }

}//Fin public class
