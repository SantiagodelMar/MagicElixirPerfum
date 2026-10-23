package gui.paneles;

import domain.Proveedor;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.ProveedorServiceInterface;

import java.awt.BorderLayout;

// Modulo de Abastecimiento - HU-15: registrar proveedores sin documentos repetidos
public class PanelProveedores extends PanelModulo {

    private final ProveedorServiceInterface proveedorService;

    private final CampoTexto txtId = new CampoTexto("Ej: 3");
    private final CampoTexto txtNombre = new CampoTexto("Razón social");
    private final CampoTexto txtDocumento = new CampoTexto("NIT sin dígito de verificación");
    private final CampoTexto txtTelefono = new CampoTexto("6044445566");
    private final CampoTexto txtCorreo = new CampoTexto("ventas@proveedor.com");
    private final CampoTexto txtDireccion = new CampoTexto("Ciudad o dirección");

    private final Boton btnGuardar = new Boton("Registrar", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEstado = new Boton("Desactivar", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos del proveedor", 3);
    private final TarjetaTabla listado = new TarjetaTabla("Proveedores", "Buscar proveedor...",
            new String[]{"ID", "Proveedor", "NIT", "Teléfono", "Correo", "Ciudad", "Estado"});

    private int idSeleccionado = -1;

    public PanelProveedores(VentanaPrincipal ventana, ProveedorServiceInterface proveedorService) {
        super(ventana, "Abastecimiento  ·  HU-15", "Proveedores",
                "Distribuidoras que le venden los perfumes a la tienda. Un proveedor inactivo no recibe órdenes de compra.");
        this.proveedorService = proveedorService;

        formulario.agregarCampo("ID", txtId);
        formulario.agregarCampo("Nombre", txtNombre);
        formulario.agregarCampo("Documento / NIT", txtDocumento);
        formulario.agregarCampo("Teléfono", txtTelefono);
        formulario.agregarCampo("Correo", txtCorreo);
        formulario.agregarCampo("Dirección", txtDireccion);

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
        tabla.anchos(45, 240, 110, 120, 230, 130, 100);
        tabla.columnaEstado(6);
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
        for (Proveedor proveedor : proveedorService.selectAllProveedores()) {
            tabla.agregarFila(
                    proveedor.getIdProveedor(),
                    proveedor.getNombre(),
                    proveedor.getDocumento(),
                    proveedor.getTelefono(),
                    proveedor.getCorreo(),
                    proveedor.getDireccion(),
                    proveedor.isActivo() ? "Activo" : "Inactivo");
        }
        listado.actualizarConteo();
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID");
            if (proveedorService.selectProveedorById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un proveedor con el ID " + id);
                return;
            }
            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String documento = ValidadorCampos.texto(txtDocumento, "Documento / NIT");
            String telefono = ValidadorCampos.telefono(txtTelefono, "El teléfono");
            String correo = ValidadorCampos.correo(txtCorreo);
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección");

            // El service devuelve null si el documento ya esta registrado
            Proveedor proveedor = proveedorService.createProveedor(id, nombre, documento, telefono, correo, direccion);
            if (proveedor == null) {
                txtDocumento.marcarError();
                mostrarError("Ya existe un proveedor con el documento " + documento);
                return;
            }

            mostrarExito("Proveedor " + nombre + " registrado");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // Se puede cambiar el telefono, el correo y la direccion
    private void actualizar() {
        try {
            String telefono = ValidadorCampos.telefono(txtTelefono, "El teléfono");
            String correo = ValidadorCampos.correo(txtCorreo);
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección");

            proveedorService.updateProveedor(idSeleccionado, telefono, correo, direccion);

            mostrarExito("Proveedor #" + idSeleccionado + " actualizado");
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cambiarEstado() {
        Proveedor proveedor = proveedorService.cambiarEstadoProveedor(idSeleccionado);
        if (proveedor != null) {
            mostrarExito(proveedor.getNombre() + " quedó " + (proveedor.isActivo() ? "activo" : "inactivo"));
            cargarTabla();
            listado.getTabla().seleccionarId(proveedor.getIdProveedor());
        }
    }

    private void eliminar() {
        boolean confirmado = confirmar("¿Eliminar proveedor?",
                "Se eliminará a " + txtNombre.getTexto() + ". Si solo dejó de trabajar con la tienda es mejor desactivarlo.",
                "Eliminar");
        if (confirmado) {
            proveedorService.deleteProveedorById(idSeleccionado);
            mostrarExito("Proveedor eliminado");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Proveedor proveedor = proveedorService.selectProveedorById(id);
        if (proveedor == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(proveedor.getIdProveedor()));
        txtNombre.setText(proveedor.getNombre());
        txtDocumento.setText(proveedor.getDocumento());
        txtTelefono.setText(proveedor.getTelefono());
        txtCorreo.setText(proveedor.getCorreo());
        txtDireccion.setText(proveedor.getDireccion());
        btnEstado.setText(proveedor.isActivo() ? "Desactivar" : "Activar");

        quitarErrores(txtId, txtNombre, txtDocumento, txtTelefono, txtCorreo, txtDireccion);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtNombre.limpiar();
        txtDocumento.limpiar();
        txtTelefono.limpiar();
        txtCorreo.limpiar();
        txtDireccion.limpiar();
        btnEstado.setText("Desactivar");

        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        txtNombre.setBloqueado(editando);
        txtDocumento.setBloqueado(editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEstado.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Al editar se puede cambiar el teléfono, el correo y la dirección.");
        } else {
            formulario.setModo(false, "Nuevo proveedor");
            formulario.setNota("No se permiten dos proveedores con el mismo documento.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (Proveedor proveedor : proveedorService.selectAllProveedores()) {
            mayor = Math.max(mayor, proveedor.getIdProveedor());
        }
        return mayor + 1;
    }

}//Fin public class
