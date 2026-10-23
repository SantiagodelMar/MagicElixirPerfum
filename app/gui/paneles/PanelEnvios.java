package gui.paneles;

import domain.Envio;
import domain.Pedido;
import domain.enums.EstadoEnvio;
import domain.enums.EstadoPedido;
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
import service.inputport.EnvioServiceInterface;
import service.inputport.PedidoServiceInterface;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Modulo de Logistica Medellin
// HU-13: registrar el envio con la guia y la transportadora
// HU-14: actualizar el estado para responderle al cliente sin tener que llamar
public class PanelEnvios extends PanelModulo {

    private final EnvioServiceInterface envioService;
    private final PedidoServiceInterface pedidoService;

    private final CampoTexto txtId = new CampoTexto("Ej: 3");
    private final Combo<Pedido> cbPedido = new Combo<>(pedido -> "#" + pedido.getIdPedido() + "  ·  "
            + pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
    private final Combo<String> cbTransportadora = new Combo<>();
    private final CampoTexto txtGuia = new CampoTexto("Ej: SV-100300");
    private final CampoTexto txtDireccion = new CampoTexto("Dirección de entrega");
    private final CampoTexto txtCosto = new CampoTexto("Ej: 12000");
    private final Combo<EstadoEnvio> cbEstado = new Combo<>(estado -> estado.getDescripcion());

    private final Boton btnGuardar = new Boton("Registrar envío", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEstado = new Boton("Actualizar estado", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos del envío", 4);
    private final TarjetaTabla listado = new TarjetaTabla("Envíos", "Rastrear por guía o cliente...",
            new String[]{"ID", "Guía", "Pedido", "Cliente", "Transportadora", "Destino", "Costo", "Enviado", "Estado"});

    private int idSeleccionado = -1;
    // Para no llenar la direccion mientras se esta cargando un envio existente
    private boolean cargando = false;

    public PanelEnvios(VentanaPrincipal ventana, EnvioServiceInterface envioService, PedidoServiceInterface pedidoService) {
        super(ventana, "Logística Medellín  ·  HU-13 y HU-14", "Envíos",
                "Guías y transportadoras de cada pedido. Al marcar un envío como entregado el pedido también queda entregado.");
        this.envioService = envioService;
        this.pedidoService = pedidoService;

        cbTransportadora.cargar(Arrays.asList("Servientrega", "Interrapidisimo", "Coordinadora", "Envia", "Mensajero propio"));
        cbEstado.cargar(Arrays.asList(EstadoEnvio.values()));

        formulario.agregarCampo("ID envío", txtId);
        formulario.agregarCampo("Pedido", cbPedido);
        formulario.agregarCampo("Transportadora", cbTransportadora);
        formulario.agregarCampo("Número de guía", txtGuia);
        formulario.agregarCampo("Dirección de destino", txtDireccion, 2);
        formulario.agregarCampo("Costo del envío", txtCosto);
        formulario.agregarCampo("Estado", cbEstado);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnEstado);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEstado.addActionListener(e -> actualizarEstado());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Al escoger el pedido se trae la direccion de envio del cliente
        cbPedido.addActionListener(e -> llenarDireccion());

        Tabla tabla = listado.getTabla();
        tabla.anchos(45, 95, 70, 140, 145, 200, 85, 100, 110);
        tabla.columnaEstado(8);
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
        for (Envio envio : envioService.selectAllEnvios()) {
            Pedido pedido = envio.getPedido();
            tabla.agregarFila(
                    envio.getIdEnvio(),
                    envio.getNumeroDeGuia(),
                    "#" + pedido.getIdPedido(),
                    pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido(),
                    envio.getTransportadora(),
                    envio.getDireccionDestino(),
                    envio.getCostoEnvio(),
                    envio.getFechaEnvio(),
                    envio.getEstadoEnvio().getDescripcion());
        }
        listado.actualizarConteo();
    }

    // En un envio nuevo solo se muestran los pedidos que todavia no se han enviado
    private void cargarPedidosParaEnviar() {
        List<Pedido> pedidos = new ArrayList<>();
        for (Pedido pedido : pedidoService.selectAllPedidos()) {
            boolean yaTieneEnvio = false;
            for (Envio envio : envioService.selectAllEnvios()) {
                if (envio.getPedido().getIdPedido() == pedido.getIdPedido()) {
                    yaTieneEnvio = true;
                }
            }
            boolean sePuedeEnviar = pedido.getEstadoPedido() == EstadoPedido.PENDIENTE
                    || pedido.getEstadoPedido() == EstadoPedido.PAGADO;
            if (sePuedeEnviar && !yaTieneEnvio) {
                pedidos.add(pedido);
            }
        }
        cargando = true;
        cbPedido.cargar(pedidos);
        cargando = false;
    }

    private void llenarDireccion() {
        Pedido pedido = cbPedido.getSeleccionado();
        if (!cargando && idSeleccionado == -1 && pedido != null) {
            txtDireccion.setText(pedido.getCliente().getDireccionEnvio());
            txtDireccion.limpiarError();
        }
    }

    // HU-13
    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID envío");
            if (envioService.selectEnvioById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un envío con el ID " + id);
                return;
            }
            Pedido pedido = ValidadorCampos.seleccion(cbPedido, "el pedido (no hay pedidos pendientes de envío)");
            String transportadora = ValidadorCampos.seleccion(cbTransportadora, "la transportadora");
            String guia = ValidadorCampos.texto(txtGuia, "Número de guía");
            if (envioService.selectEnvioByGuia(guia) != null) {
                txtGuia.marcarError();
                mostrarError("Ya hay un envío con la guía " + guia);
                return;
            }
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección de destino");
            double costo = ValidadorCampos.dinero(txtCosto, "El costo del envío");

            Envio envio = envioService.createEnvio(id, pedido.getIdPedido(), transportadora, guia, direccion, costo);
            if (envio == null) {
                mostrarError("No se encontró el pedido #" + pedido.getIdPedido());
                return;
            }

            mostrarExito("Envío registrado. El pedido #" + pedido.getIdPedido() + " pasó a Enviado");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            String transportadora = ValidadorCampos.seleccion(cbTransportadora, "la transportadora");
            String guia = ValidadorCampos.texto(txtGuia, "Número de guía");
            Envio otro = envioService.selectEnvioByGuia(guia);
            if (otro != null && otro.getIdEnvio() != idSeleccionado) {
                txtGuia.marcarError();
                mostrarError("Ya hay otro envío con la guía " + guia);
                return;
            }
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección de destino");
            double costo = ValidadorCampos.dinero(txtCosto, "El costo del envío");

            envioService.updateEnvio(idSeleccionado, transportadora, guia, direccion, costo);

            mostrarExito("Envío #" + idSeleccionado + " actualizado");
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // HU-14
    private void actualizarEstado() {
        EstadoEnvio nuevoEstado = cbEstado.getSeleccionado();
        Envio envio = envioService.actualizarEstadoEnvio(idSeleccionado, nuevoEstado);
        if (envio == null) {
            return;
        }

        if (nuevoEstado == EstadoEnvio.ENTREGADO) {
            mostrarExito("Envío entregado. El pedido #" + envio.getPedido().getIdPedido() + " quedó Entregado");
        } else {
            mostrarExito("El envío " + envio.getNumeroDeGuia() + " ahora está: " + nuevoEstado.getDescripcion());
        }
        int id = idSeleccionado;
        cargarTabla();
        listado.getTabla().seleccionarId(id);
    }

    private void eliminar() {
        boolean confirmado = confirmar("¿Eliminar envío?",
                "Se borrará el envío #" + idSeleccionado + " con guía " + txtGuia.getTexto() + ".", "Eliminar");
        if (confirmado) {
            envioService.deleteEnvioById(idSeleccionado);
            mostrarExito("Envío eliminado");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Envio envio = envioService.selectEnvioById(id);
        if (envio == null) {
            return;
        }
        idSeleccionado = id;

        // En edicion se muestra el pedido del envio aunque ya este enviado
        cargando = true;
        cbPedido.cargar(pedidoService.selectAllPedidos());
        cbPedido.setSelectedItem(envio.getPedido());
        cargando = false;

        txtId.setText(String.valueOf(envio.getIdEnvio()));
        cbTransportadora.seleccionar(envio.getTransportadora());
        txtGuia.setText(envio.getNumeroDeGuia());
        txtDireccion.setText(envio.getDireccionDestino());
        txtCosto.setText(String.valueOf((long) envio.getCostoEnvio()));
        cbEstado.setSelectedItem(envio.getEstadoEnvio());

        quitarErrores(txtId, txtGuia, txtDireccion, txtCosto);
        modoEdicion(true);
        if (envio.getFechaEntrega() != null) {
            formulario.setNota("Entregado el " + Formato.fecha(envio.getFechaEntrega()) + ".");
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtGuia.limpiar();
        txtDireccion.limpiar();
        txtCosto.limpiar();
        cbTransportadora.setSelectedIndex(0);
        cbEstado.setSelectedItem(EstadoEnvio.CREADO);

        cargarPedidosParaEnviar();
        llenarDireccion();

        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        cbPedido.setBloqueado(editando);
        cbEstado.setBloqueado(!editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEstado.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Cambia el estado y dale \"Actualizar estado\".");
        } else {
            formulario.setModo(false, "Nuevo envío");
            formulario.setNota("Solo salen pedidos pendientes o pagados sin envío.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (Envio envio : envioService.selectAllEnvios()) {
            mayor = Math.max(mayor, envio.getIdEnvio());
        }
        return mayor + 1;
    }

}//Fin public class
