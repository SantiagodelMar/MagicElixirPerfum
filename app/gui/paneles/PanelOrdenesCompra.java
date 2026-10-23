package gui.paneles;

import domain.OrdenCompra;
import domain.Proveedor;
import domain.enums.EstadoOrdenCompra;
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
import service.inputport.OrdenCompraServiceInterface;
import service.inputport.ProveedorServiceInterface;

import java.awt.BorderLayout;
import java.util.Arrays;

// Modulo de Abastecimiento - HU-16: hacerle ordenes de compra a los proveedores activos
public class PanelOrdenesCompra extends PanelModulo {

    private final OrdenCompraServiceInterface ordenCompraService;
    private final ProveedorServiceInterface proveedorService;

    private final CampoTexto txtId = new CampoTexto("Ej: 3");
    private final Combo<Proveedor> cbProveedor = new Combo<>(proveedor ->
            proveedor.getNombre() + (proveedor.isActivo() ? "" : "  (inactivo)"));
    private final CampoTexto txtTotal = new CampoTexto("Ej: 3500000");
    private final Combo<EstadoOrdenCompra> cbEstado = new Combo<>(estado -> estado.getDescripcion());
    private final CampoTexto txtObservaciones = new CampoTexto("Qué se pidió, ej: reposición de Lattafa");

    private final Boton btnGuardar = new Boton("Crear orden", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEstado = new Boton("Cambiar estado", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos de la orden", 4);
    private final TarjetaTabla listado = new TarjetaTabla("Órdenes de compra", "Buscar proveedor o estado...",
            new String[]{"ID", "Fecha", "Proveedor", "Total", "Observaciones", "Estado"});

    private int idSeleccionado = -1;

    public PanelOrdenesCompra(VentanaPrincipal ventana, OrdenCompraServiceInterface ordenCompraService,
                              ProveedorServiceInterface proveedorService) {
        super(ventana, "Abastecimiento  ·  HU-16", "Órdenes de compra",
                "Pedidos de mercancía a los proveedores. Toda orden empieza pendiente hasta que se aprueba y se recibe.");
        this.ordenCompraService = ordenCompraService;
        this.proveedorService = proveedorService;

        cbEstado.cargar(Arrays.asList(EstadoOrdenCompra.values()));

        formulario.agregarCampo("ID orden", txtId);
        formulario.agregarCampo("Proveedor", cbProveedor);
        formulario.agregarCampo("Total", txtTotal);
        formulario.agregarCampo("Estado", cbEstado);
        formulario.agregarCampo("Observaciones", txtObservaciones, 4);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnEstado);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEstado.addActionListener(e -> cambiarEstado());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        Tabla tabla = listado.getTabla();
        tabla.anchos(45, 100, 230, 130, 300, 110);
        tabla.columnaEstado(5);
        tabla.alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
    }

    @Override
    public void refrescar() {
        cbProveedor.cargar(proveedorService.selectAllProveedores());
        cargarTabla();
        limpiarFormulario();
    }

    private void cargarTabla() {
        Tabla tabla = listado.getTabla();
        tabla.limpiar();
        for (OrdenCompra orden : ordenCompraService.selectAllOrdenesCompra()) {
            tabla.agregarFila(
                    orden.getIdOrdenCompra(),
                    orden.getFechaOrden(),
                    orden.getProveedor().getNombre(),
                    orden.getTotal(),
                    orden.getObservaciones(),
                    orden.getEstado().getDescripcion());
        }
        listado.actualizarConteo();
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID orden");
            if (ordenCompraService.selectOrdenCompraById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe una orden con el ID " + id);
                return;
            }
            Proveedor proveedor = ValidadorCampos.seleccion(cbProveedor, "el proveedor");
            if (!proveedor.isActivo()) {
                cbProveedor.marcarError();
                mostrarError(proveedor.getNombre() + " está inactivo. Actívalo en Proveedores");
                return;
            }
            double total = ValidadorCampos.dinero(txtTotal, "El total");
            String observaciones = ValidadorCampos.texto(txtObservaciones, "Observaciones");

            OrdenCompra orden = ordenCompraService.createOrdenCompra(id, proveedor.getIdProveedor(), total, observaciones);
            if (orden == null) {
                mostrarError("No se pudo crear la orden, revisa el proveedor y el total");
                return;
            }

            mostrarExito("Orden #" + id + " por " + Formato.dinero(total) + " creada");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            double total = ValidadorCampos.dinero(txtTotal, "El total");
            String observaciones = ValidadorCampos.texto(txtObservaciones, "Observaciones");

            ordenCompraService.updateOrdenCompra(idSeleccionado, total, observaciones);

            mostrarExito("Orden #" + idSeleccionado + " actualizada");
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cambiarEstado() {
        EstadoOrdenCompra nuevoEstado = cbEstado.getSeleccionado();
        OrdenCompra orden = ordenCompraService.cambiarEstadoOrdenCompra(idSeleccionado, nuevoEstado);
        if (orden != null) {
            mostrarExito("Orden #" + idSeleccionado + ": " + nuevoEstado.getDescripcion().toLowerCase());
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        }
    }

    private void eliminar() {
        boolean confirmado = confirmar("¿Eliminar orden de compra?",
                "Se borrará la orden #" + idSeleccionado + ".", "Eliminar");
        if (confirmado) {
            ordenCompraService.deleteOrdenCompraById(idSeleccionado);
            mostrarExito("Orden eliminada");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        OrdenCompra orden = ordenCompraService.selectOrdenCompraById(id);
        if (orden == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(orden.getIdOrdenCompra()));
        cbProveedor.seleccionar(orden.getProveedor());
        txtTotal.setText(String.valueOf((long) orden.getTotal()));
        cbEstado.setSelectedItem(orden.getEstado());
        txtObservaciones.setText(orden.getObservaciones());

        quitarErrores(txtId, txtTotal, txtObservaciones);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtTotal.limpiar();
        txtObservaciones.limpiar();
        if (cbProveedor.getItemCount() > 0) {
            cbProveedor.setSelectedIndex(0);
        }
        cbEstado.setSelectedItem(EstadoOrdenCompra.PENDIENTE);

        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        cbProveedor.setBloqueado(editando);
        cbEstado.setBloqueado(!editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEstado.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Escoge el estado y dale \"Cambiar estado\".");
        } else {
            formulario.setModo(false, "Nueva orden");
            formulario.setNota("Solo se le puede pedir a proveedores activos.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (OrdenCompra orden : ordenCompraService.selectAllOrdenesCompra()) {
            mayor = Math.max(mayor, orden.getIdOrdenCompra());
        }
        return mayor + 1;
    }

}//Fin public class
