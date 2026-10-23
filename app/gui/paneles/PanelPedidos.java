package gui.paneles;

import domain.Cliente;
import domain.DetallePedido;
import domain.Inventario;
import domain.Pedido;
import domain.Perfume;
import domain.enums.EstadoPedido;
import domain.enums.MetodoDePago;
import gui.VentanaPrincipal;
import gui.componentes.BarraScroll;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Combo;
import gui.componentes.EtiquetaEstado;
import gui.componentes.PanelRedondo;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.estilo.Estilo;
import gui.estilo.Formato;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.ClienteServiceInterface;
import service.inputport.InventarioServiceInterface;
import service.inputport.PedidoServiceInterface;
import service.inputport.PerfumeServiceInterface;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;

// Modulo de Pedidos y Pagos
// HU-09: crear el pedido del cliente   HU-10 y HU-11: agregar perfumes y calcular el total
// HU-08: validar el stock              HU-12: registrar el metodo de pago
public class PanelPedidos extends PanelModulo {

    private final PedidoServiceInterface pedidoService;
    private final ClienteServiceInterface clienteService;
    private final PerfumeServiceInterface perfumeService;
    private final InventarioServiceInterface inventarioService;

    // Formulario del pedido
    private final CampoTexto txtId = new CampoTexto("Ej: 4");
    private final Combo<Cliente> cbCliente = new Combo<>(cliente -> cliente.getNombre() + " " + cliente.getApellido());
    private final Combo<MetodoDePago> cbMetodoPago = new Combo<>(metodo -> metodo.getDescripcion());
    private final Combo<EstadoPedido> cbEstado = new Combo<>(estado -> Formato.nombreEnum(estado));
    private final CampoTexto txtObservaciones = new CampoTexto("Ej: empacar para regalo, entregar en portería...");

    private final Boton btnCrear = new Boton("Crear pedido", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEstado = new Boton("Cambiar estado", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    // Detalle del pedido (los perfumes que lleva)
    private final Combo<Perfume> cbPerfume = new Combo<>(perfume ->
            perfume.getNombre() + "  ·  " + perfume.getMarca() + "  ·  " + Formato.dinero(perfume.getPrecio()));
    private final CampoTexto txtCantidad = new CampoTexto("Cant.");
    private final Boton btnAgregar = new Boton("Agregar", Boton.PRINCIPAL);
    private final JLabel lblStock = Estilo.texto(" ", 12.5f, Estilo.GRIS);
    private final JLabel lblTituloDetalle = Estilo.micro("Detalle del pedido");
    private final EtiquetaEstado etiquetaEstado = new EtiquetaEstado("");
    private final Tabla tablaDetalle = new Tabla(new String[]{"#", "Perfume", "Cant", "Subtotal"});
    private final JLabel lblTotal = Estilo.titulo(Formato.dinero(0), 24f);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos del pedido", 6);
    private final TarjetaTabla listado = new TarjetaTabla("Pedidos", "Buscar cliente o estado...",
            new String[]{"ID", "Fecha", "Cliente", "Total", "Estado"});

    private int idSeleccionado = -1;

    public PanelPedidos(VentanaPrincipal ventana, PedidoServiceInterface pedidoService,
                        ClienteServiceInterface clienteService, PerfumeServiceInterface perfumeService,
                        InventarioServiceInterface inventarioService) {
        super(ventana, "Pedidos y pagos  ·  HU-08 a HU-12", "Pedidos",
                "Crea el pedido, agrégale los perfumes y el sistema calcula el total y descuenta del inventario.");
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.perfumeService = perfumeService;
        this.inventarioService = inventarioService;

        armarFormulario();

        Tabla tabla = listado.getTabla();
        tabla.anchos(45, 100, 180, 110, 115);
        tabla.columnaEstado(4);
        tabla.alSeleccionarFila(id -> cargarEnFormulario(id));

        // Abajo: la lista de pedidos y a la derecha el detalle del seleccionado
        JPanel abajo = new JPanel(new BorderLayout(16, 0));
        abajo.setOpaque(false);
        abajo.add(listado, BorderLayout.CENTER);
        abajo.add(crearTarjetaDetalle(), BorderLayout.EAST);

        add(unirFormularioYTabla(formulario, abajo), BorderLayout.CENTER);
    }

    private void armarFormulario() {
        cbMetodoPago.cargar(Arrays.asList(MetodoDePago.values()));
        cbEstado.cargar(Arrays.asList(EstadoPedido.values()));

        formulario.agregarCampo("ID pedido", txtId);
        formulario.agregarCampo("Cliente", cbCliente);
        formulario.agregarCampo("Método de pago", cbMetodoPago);
        formulario.agregarCampo("Estado", cbEstado);
        formulario.agregarCampo("Observaciones", txtObservaciones, 2);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnEstado);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnCrear);

        btnCrear.addActionListener(e -> crear());
        btnActualizar.addActionListener(e -> actualizar());
        btnEstado.addActionListener(e -> cambiarEstado());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private PanelRedondo crearTarjetaDetalle() {
        PanelRedondo tarjeta = new PanelRedondo(new BorderLayout(0, 12), 16);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        tarjeta.setPreferredSize(new Dimension(440, 0));

        // Encabezado
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        lblTituloDetalle.setForeground(Estilo.TINTA);
        encabezado.add(lblTituloDetalle, BorderLayout.WEST);
        encabezado.add(etiquetaEstado, BorderLayout.EAST);

        // Fila para agregar perfumes: cantidad, stock y boton
        txtCantidad.setPreferredSize(new Dimension(76, 38));
        lblStock.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        JPanel filaAgregar = new JPanel(new BorderLayout());
        filaAgregar.setOpaque(false);
        filaAgregar.add(txtCantidad, BorderLayout.WEST);
        filaAgregar.add(lblStock, BorderLayout.CENTER);
        filaAgregar.add(btnAgregar, BorderLayout.EAST);

        cbPerfume.addActionListener(e -> mostrarStockDelPerfume());
        btnAgregar.addActionListener(e -> agregarPerfume());

        JPanel arriba = new JPanel();
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        arriba.setOpaque(false);
        encabezado.setAlignmentX(LEFT_ALIGNMENT);
        cbPerfume.setAlignmentX(LEFT_ALIGNMENT);
        filaAgregar.setAlignmentX(LEFT_ALIGNMENT);
        cbPerfume.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        filaAgregar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        arriba.add(encabezado);
        arriba.add(Box.createVerticalStrut(14));
        arriba.add(cbPerfume);
        arriba.add(Box.createVerticalStrut(8));
        arriba.add(filaAgregar);

        // Tabla con los perfumes del pedido
        tablaDetalle.anchos(44, 190, 66, 110);
        JScrollPane scroll = new JScrollPane(tablaDetalle);
        BarraScroll.aplicar(scroll, Estilo.BLANCO, new Color(0xCF, 0xCB, 0xC4));

        // Total abajo
        JPanel filaTotal = new JPanel(new BorderLayout());
        filaTotal.setOpaque(false);
        filaTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Estilo.LINEA),
                BorderFactory.createEmptyBorder(12, 0, 0, 0)));
        filaTotal.add(Estilo.micro("Total del pedido"), BorderLayout.WEST);
        filaTotal.add(lblTotal, BorderLayout.EAST);

        tarjeta.add(arriba, BorderLayout.NORTH);
        tarjeta.add(scroll, BorderLayout.CENTER);
        tarjeta.add(filaTotal, BorderLayout.SOUTH);
        return tarjeta;
    }

    @Override
    public void refrescar() {
        cbCliente.cargar(clienteService.selectAllClientes());
        cbPerfume.cargar(perfumeService.selectAllPerfumes());
        cargarTabla();
        limpiarFormulario();
    }

    private void cargarTabla() {
        Tabla tabla = listado.getTabla();
        tabla.limpiar();
        for (Pedido pedido : pedidoService.selectAllPedidos()) {
            tabla.agregarFila(
                    pedido.getIdPedido(),
                    pedido.getFechaPedido(),
                    pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido(),
                    pedido.getTotalCostoPedido(),
                    Formato.nombreEnum(pedido.getEstadoPedido()));
        }
        listado.actualizarConteo();
    }

    // HU-09 y HU-12
    private void crear() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID pedido");
            if (pedidoService.selectPedidoById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un pedido con el ID " + id);
                return;
            }
            Cliente cliente = ValidadorCampos.seleccion(cbCliente, "el cliente");
            MetodoDePago metodo = ValidadorCampos.seleccion(cbMetodoPago, "el método de pago");
            String observaciones = txtObservaciones.getTexto().isEmpty() ? "Sin observaciones" : txtObservaciones.getTexto();

            Pedido pedido = pedidoService.createPedido(id, cliente.getIdUsuario(), metodo, observaciones);
            if (pedido == null) {
                mostrarError("No se pudo crear el pedido, revisa el cliente");
                return;
            }

            mostrarExito("Pedido #" + id + " creado. Ahora agrégale los perfumes");
            cargarTabla();
            // Se deja seleccionado para seguir con el detalle
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            MetodoDePago metodo = ValidadorCampos.seleccion(cbMetodoPago, "el método de pago");
            String observaciones = txtObservaciones.getTexto().isEmpty() ? "Sin observaciones" : txtObservaciones.getTexto();

            pedidoService.updatePedido(idSeleccionado, metodo, observaciones);

            mostrarExito("Pedido #" + idSeleccionado + " actualizado");
            recargarYSeleccionar(idSeleccionado);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cambiarEstado() {
        EstadoPedido nuevoEstado = cbEstado.getSeleccionado();
        Pedido pedido = pedidoService.selectPedidoById(idSeleccionado);
        if (pedido == null || nuevoEstado == null) {
            return;
        }
        if (pedido.getEstadoPedido() == nuevoEstado) {
            mostrarError("El pedido ya está en estado " + Formato.nombreEnum(nuevoEstado).toLowerCase());
            return;
        }

        // Si se cancela, el service devuelve los perfumes al inventario
        if (nuevoEstado == EstadoPedido.CANCELADO) {
            boolean confirmado = confirmar("¿Cancelar el pedido?",
                    "Los perfumes del pedido #" + idSeleccionado + " vuelven al inventario.", "Cancelar pedido");
            if (!confirmado) {
                return;
            }
        }

        pedidoService.cambiarEstadoPedido(idSeleccionado, nuevoEstado);
        mostrarExito("Pedido #" + idSeleccionado + ": " + nuevoEstado.getDescripcion());
        recargarYSeleccionar(idSeleccionado);
    }

    private void eliminar() {
        boolean confirmado = confirmar("¿Eliminar pedido?",
                "Se borrará el pedido #" + idSeleccionado + ". Ojo: eliminar no devuelve los perfumes al inventario, "
                        + "para eso es mejor cambiar el estado a Cancelado.", "Eliminar");
        if (confirmado) {
            pedidoService.deletePedidoById(idSeleccionado);
            mostrarExito("Pedido eliminado");
            cargarTabla();
            limpiarFormulario();
        }
    }

    // HU-10, HU-11 y HU-08: el service valida el stock, lo descuenta y recalcula el total
    private void agregarPerfume() {
        try {
            Perfume perfume = ValidadorCampos.seleccion(cbPerfume, "el perfume");
            int cantidad = ValidadorCampos.entero(txtCantidad, "La cantidad");

            String respuesta = pedidoService.agregarPerfume(idSeleccionado, perfume.getIdPerfume(), cantidad);

            // El service devuelve un texto: si empieza asi es porque salio bien
            if (respuesta.startsWith("Perfume agregado")) {
                mostrarExito(cantidad + " x " + perfume.getNombre() + " agregado al pedido");
                txtCantidad.setText("1");
                recargarYSeleccionar(idSeleccionado);
            } else {
                mostrarError(respuesta);
            }
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void recargarYSeleccionar(int id) {
        cargarTabla();
        listado.getTabla().seleccionarId(id);
    }

    private void cargarEnFormulario(int id) {
        Pedido pedido = pedidoService.selectPedidoById(id);
        if (pedido == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(pedido.getIdPedido()));
        cbCliente.seleccionar(pedido.getCliente());
        cbMetodoPago.setSelectedItem(pedido.getMetodoPago());
        cbEstado.setSelectedItem(pedido.getEstadoPedido());
        txtObservaciones.setText(pedido.getObservaciones());
        quitarErrores(txtId, txtObservaciones, txtCantidad);

        modoEdicion(true);
        cargarDetalle(pedido);
    }

    private void cargarDetalle(Pedido pedido) {
        tablaDetalle.limpiar();
        if (pedido == null) {
            lblTituloDetalle.setText("DETALLE DEL PEDIDO");
            etiquetaEstado.setVisible(false);
            lblTotal.setText(Formato.dinero(0));
            habilitarAgregar(false);
            lblStock.setText("Selecciona un pedido");
            lblStock.setForeground(Estilo.GRIS);
            return;
        }

        lblTituloDetalle.setText("DETALLE DEL PEDIDO #" + pedido.getIdPedido());
        etiquetaEstado.setEstado(Formato.nombreEnum(pedido.getEstadoPedido()));
        etiquetaEstado.setVisible(true);

        for (DetallePedido detalle : pedido.getDetalles()) {
            tablaDetalle.agregarFila(
                    detalle.getIdDetallePedido(),
                    detalle.getPerfume().getNombre(),
                    detalle.getCantidad(),
                    detalle.getSubtotal());
        }
        lblTotal.setText(Formato.dinero(pedido.getTotalCostoPedido()));

        // Solo a los pedidos pendientes se les pueden agregar perfumes
        boolean pendiente = pedido.getEstadoPedido() == EstadoPedido.PENDIENTE;
        habilitarAgregar(pendiente);
        if (pendiente) {
            mostrarStockDelPerfume();
        } else {
            lblStock.setText("Solo pedidos pendientes");
            lblStock.setForeground(Estilo.GRIS);
        }
    }

    private void habilitarAgregar(boolean habilitar) {
        cbPerfume.setBloqueado(!habilitar);
        txtCantidad.setBloqueado(!habilitar);
        btnAgregar.setEnabled(habilitar);
    }

    // HU-08: muestra cuantas unidades hay del perfume escogido
    private void mostrarStockDelPerfume() {
        Perfume perfume = cbPerfume.getSeleccionado();
        if (perfume == null || !btnAgregar.isEnabled()) {
            return;
        }
        Inventario inventario = inventarioService.selectInventarioByPerfume(perfume.getIdPerfume());
        if (inventario == null) {
            lblStock.setText("Sin inventario registrado");
            lblStock.setForeground(Estilo.ROJO);
        } else if (inventario.getStockDisponible() == 0) {
            lblStock.setText("Agotado");
            lblStock.setForeground(Estilo.ROJO);
        } else {
            lblStock.setText(inventario.getStockDisponible() + " und. disponibles");
            lblStock.setForeground(Estilo.GRIS);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtObservaciones.limpiar();
        txtCantidad.setText("1");
        if (cbCliente.getItemCount() > 0) {
            cbCliente.setSelectedIndex(0);
        }
        cbMetodoPago.setSelectedIndex(0);
        cbEstado.setSelectedItem(EstadoPedido.PENDIENTE);

        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
        cargarDetalle(null);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        cbCliente.setBloqueado(editando);
        // El estado se cambia solo con el boton "Cambiar estado" y cuando el pedido ya existe
        cbEstado.setBloqueado(!editando);

        btnCrear.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEstado.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Se puede cambiar el pago, las observaciones y el estado.");
        } else {
            formulario.setModo(false, "Nuevo pedido");
            formulario.setNota("Todo pedido nuevo empieza en estado Pendiente.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (Pedido pedido : pedidoService.selectAllPedidos()) {
            mayor = Math.max(mayor, pedido.getIdPedido());
        }
        return mayor + 1;
    }

}//Fin public class
